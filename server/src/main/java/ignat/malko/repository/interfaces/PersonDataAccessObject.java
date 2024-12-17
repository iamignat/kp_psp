package ignat.malko.repository.interfaces;

import ignat.malko.model.PersonData;

public interface PersonDataAccessObject extends DataAccessObject<PersonData> {
    PersonData findByPhoneNumber(String phoneNumber);
}
