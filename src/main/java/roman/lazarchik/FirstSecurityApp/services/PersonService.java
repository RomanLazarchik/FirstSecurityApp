package roman.lazarchik.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roman.lazarchik.FirstSecurityApp.models.Person;
import roman.lazarchik.FirstSecurityApp.repositories.PeopleRepository;

import java.util.Optional;

@Service
public class PersonService {

    private final PeopleRepository userRepository;

    @Autowired
    public PersonService(PeopleRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Person> findOneByName(String name) {
        return userRepository.findByUsername(name).stream().findAny();
    }

}
