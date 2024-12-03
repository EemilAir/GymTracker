package backendsyksy24.gymtracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import backendsyksy24.gymtracker.model.AppUser;
import backendsyksy24.gymtracker.repository.AppUserRepository;

// Authenticates users by loading their details from the database and providing the necessary information for security checks.
@Service
public class UserDetailServiceImpl implements UserDetailsService  {
	
	@Autowired
	AppUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   
    	AppUser curruser = repository.findByUsername(username);
        UserDetails user = new org.springframework.security.core.userdetails.User(username, curruser.getPasswordHash(), 
        		AuthorityUtils.createAuthorityList(curruser.getRole())); 
        return user;
    }   
} 
