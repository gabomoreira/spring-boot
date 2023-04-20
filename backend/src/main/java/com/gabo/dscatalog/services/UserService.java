package com.gabo.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabo.dscatalog.dto.RoleDTO;
import com.gabo.dscatalog.dto.UserDTO;
import com.gabo.dscatalog.dto.UserInsertDTO;
import com.gabo.dscatalog.dto.UserUpdateDTO;
import com.gabo.dscatalog.entities.Role;
import com.gabo.dscatalog.entities.User;
import com.gabo.dscatalog.repositories.RoleRepository;
import com.gabo.dscatalog.repositories.UserRepository;
import com.gabo.dscatalog.services.exceptions.DatabaseException;
import com.gabo.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Transactional(readOnly = true)
	public List<UserDTO> findAll() {
		List<User> list = repository.findAll();
		
		return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		
		return list.map(x -> new UserDTO(x));
	}
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
		
		return new UserDTO(user);
	}
	
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User user = new User(); 
		copyDtoToEntity(dto, user);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user = repository.save(user);
		
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO update(UserUpdateDTO userDto, Long id) {
		try {
			User user = repository.getOne(id);
			copyDtoToEntity(userDto, user);
			user = repository.save(user);
			
			return new UserDTO(user);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	
	public void copyDtoToEntity(UserDTO userDto, User user) {
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		
		user.getRoles().clear();
		for ( RoleDTO roleDto : userDto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			user.getRoles().add(role);
		}
	}
}













