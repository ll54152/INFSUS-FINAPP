package com.infsus.finapp.rest;

import com.infsus.finapp.domain.Person;
import com.infsus.finapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
public class PersonUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonService akService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person korisnik = akService.findByEmail(username.toLowerCase());
        if (korisnik != null) {
            return new User(
                    korisnik.getEmail(),
                    korisnik.getPassword(),
                    commaSeparatedStringToAuthorityList("ROLE_USER")
            );
        } else {
            throw new UsernameNotFoundException("Ne postoji korisnik " + username);
        }
    }
}
