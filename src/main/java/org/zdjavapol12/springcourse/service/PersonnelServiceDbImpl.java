package org.zdjavapol12.springcourse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zdjavapol12.springcourse.exception.WrongPageException;
import org.zdjavapol12.springcourse.model.Personnel;
import org.zdjavapol12.springcourse.repository.PersonnelRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Profile("!old")
@Service
@Scope("prototype")
@Slf4j
public class PersonnelServiceDbImpl implements PersonnelService {

    private final PersonnelRepository personnelRepository;

    public PersonnelServiceDbImpl(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    @Override
    public Personnel getPersonnelById(Long id) {
        return personnelRepository.findById(id).orElse(null);
    }

    @Override
    public List<Personnel> getAllPersonnel(Integer page, Integer size) throws WrongPageException {
        if(!Objects.nonNull(page)){
            page = 1;
        }
        if (!Objects.nonNull(size)){
            size = 5;
        }
        if(page < 1){
            throw new WrongPageException("Strona nie moze byc mniejsza niz 1");
        }
        Sort sort = Sort.by("salary").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return personnelRepository.findAll(pageable).getContent();
    }

    @Override
    public boolean removePersonnelById(Long id) {
        personnelRepository.deleteById(id);
        return true;
    }

    @Override
    public Personnel createNewPersonnel(Personnel personnel) {
        log.info("Tworze nowy personel");
        return personnelRepository.save(personnel);
    }

    @Override
    public List<Personnel> createBatchOfPersonnel(List<Personnel> personnels) {
        return personnelRepository.saveAll(personnels);
    }

    @Override
    public Personnel updatePersonnelById(Long id, Personnel personnel) {
        return null;
    }

    @Override
    public List<Personnel> getPersonnelBySickLeave(Boolean sickLeave) {
        return personnelRepository.findPersonnelsBySickLeaveEquals(sickLeave);
    }

    @Override
    public List<Personnel> getPersonnelByPosition(String position) {
        return personnelRepository.selectAllPersonnelWithPositionEqualTo(position);
    }

    @Override
    public void cureAllPersonnel() {
        personnelRepository.updateAllPersonnelToBeHealthy();
    }
}
