package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.UserDTO;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.integration.MailSending;
import com.ericsson.rampup.repositories.RoleRepository;
import com.ericsson.rampup.repositories.UserRepository;
import com.ericsson.rampup.services.exceptions.EmailAlreadyRegisteredExeption;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import com.ericsson.rampup.services.exceptions.NotFoundExeption;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MailSending mailSending;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final String INVALID_EMAIL_MESSAGE = "Must insert a valid email.";
	private static final String INVALID_PASSWORD_MESSAGE = "Must insert a valid password.";

	public List<User> findAll(int page) {
		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, 10, sort);
		return repository.findAll(pageable).stream().toList();
	}

	public User findById(Long id) {
		Optional<User> obj = repository.findById(id);
		return obj.orElseThrow(() -> new IdNotFoundExeption(id));
	}

	public UserDTO findByEmail(String email){
		Optional<User> obj = repository.findByEmail(email);
		if(obj.isPresent()){
			return new UserDTO(obj.get());
		}
		throw  new UsernameNotFoundException("Non-Existent Email: " + email);
	}

	public User insert(UserDTO objDto){
		try {
			User obj = fromDTO(objDto);
			validateFields(obj);
			passwordEncoder = new BCryptPasswordEncoder();
			obj.setPassword(passwordEncoder.encode(obj.getPassword()));
			roleRepository.save(obj.getRoles().get(0));
			mailSending.sendMail(obj.getEmail(), objDto.getPassword());
			return repository.save(obj);
		}catch (MessagingException e){
			throw new NotFoundExeption(e.getMessage());
		}catch (EmailAlreadyRegisteredExeption e){
			throw new EmailAlreadyRegisteredExeption(INVALID_EMAIL_MESSAGE);
		}
	}

	@PreAuthorize("hasRole('0')")
	public User insertAdm(UserDTO objDto){
		User obj = fromDtoAdm(objDto);
		validateFields(obj);
		passwordEncoder = new BCryptPasswordEncoder();
		obj.setPassword(passwordEncoder.encode(obj.getPassword()));
		roleRepository.save(obj.getRoles().get(0));
		return repository.save(obj);
	}

	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}

	@Transactional
	public User update(Long id, User obj) {
		try {
			User entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new IdNotFoundExeption(id);
		}

	}

	private void updateData(User entity, User obj) {
		if(obj.getEmail() != null){
			entity.setEmail(obj.getEmail());
		}
		if(obj.getPassword() != null){
			entity.setPassword(passwordEncoder.encode(obj.getPassword()));
			if(entity.getCustomer() != null){
				entity.getCustomer().setPassword(obj.getPassword());
			}
		}
	}

	public User fromDTO(UserDTO objDTO) {
		return new User(objDTO.getId(), objDTO.getEmail(), objDTO.getPassword());
	}

	private User fromDtoAdm(UserDTO objDTO) {
		return new User(objDTO.getEmail(), objDTO.getPassword());
	}

	private List<User> listAll() {
		return repository.findAll();
	}

	public void validateFields(User user) {
		if (user.getEmail().isBlank()) {
			throw new IllegalArgumentException(INVALID_EMAIL_MESSAGE);
		}
		if (user.getPassword().isBlank()) {
			throw new IllegalArgumentException(INVALID_PASSWORD_MESSAGE);
		}
		if (emailMatching(this.listAll(), user)) {
			throw new EmailAlreadyRegisteredExeption(INVALID_EMAIL_MESSAGE);
		}
	}

	public boolean emailMatching(List<User> list, User user) {
		Stream<User> userStream = list.stream();
		return userStream.anyMatch(x -> Objects.equals(x.getEmail(), user.getEmail()));
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Non-Existent Login"));

		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getEmail()).password(user.getPassword())
				.roles(user.getRolesinString()).build();
	}

	public int totalOfUsers(){
		List<Object[]> count = repository.totalOfUsers();

		if(count != null && !count.isEmpty()){
			return Integer.parseInt(count.get(0)[0].toString());
		}
		return 0;
	}
}
