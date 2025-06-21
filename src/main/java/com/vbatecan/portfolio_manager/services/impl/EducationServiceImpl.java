package com.vbatecan.portfolio_manager.services.impl;

import com.vbatecan.portfolio_manager.exception.ConflictException;
import com.vbatecan.portfolio_manager.mappers.EducationInputMapper;
import com.vbatecan.portfolio_manager.mappers.EducationMapper;
import com.vbatecan.portfolio_manager.models.dto.EducationDTO;
import com.vbatecan.portfolio_manager.models.entities.Education;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filter.EducationFilterInput;
import com.vbatecan.portfolio_manager.models.input.EducationInput;
import com.vbatecan.portfolio_manager.repositories.EducationRepository;
import com.vbatecan.portfolio_manager.services.interfaces.AuthService;
import com.vbatecan.portfolio_manager.services.interfaces.EducationService;
import com.vbatecan.portfolio_manager.specs.EducationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;
    private final EducationInputMapper educationInputMapper;
    private final AuthService authService;

    @Override
    public Page<EducationDTO> listAll(@NonNull Pageable pageable) {
        User user = authService.getLoggedInUser();
        Page<Education> educations = educationRepository.findByUser_Id(user.getId(), pageable);
        return educations.map(educationMapper::toDto);
    }

    @Override
    public Optional<EducationDTO> get(@NonNull UUID id) {
        User user = authService.getLoggedInUser();
        return educationRepository.findByIdAndUser_Id(id, user.getId())
                .map(educationMapper::toDto);
    }

    @Override
    public Optional<EducationDTO> save(@NonNull EducationInput educationInput) {
        User user = authService.getLoggedInUser();

        if (educationRepository.existsByTitleAndInstitutionAndUser_Id(educationInput.title(), educationInput.institution(), user.getId())) {
            throw new ConflictException("Education with the same title and institution already exists for this user.");
        }

        Education education = educationInputMapper.toEntity(educationInput);
        education.setUser(user);
        Education savedEducation = educationRepository.save(education);
        return Optional.of(educationMapper.toDto(savedEducation));
    }

    @Override
    public Optional<EducationDTO> update(@NonNull UUID id, @NonNull EducationInput educationInput) {
        User user = authService.getLoggedInUser();
        Optional<Education> existingEducationOpt = educationRepository.findByIdAndUser_Id(id, user.getId());

        return existingEducationOpt.map(education -> {
            if (!education.getTitle().equals(educationInput.title()) || !education.getInstitution().equals(educationInput.institution())) {
                if (educationRepository.existsByTitleAndInstitutionAndUser_Id(educationInput.title(), educationInput.institution(), user.getId())) {
                    throw new ConflictException("Another education with the same title and institution already exists for this user.");
                }
            }

            educationInputMapper.updateEntityFromInput(educationInput, education);
            Education updatedEducation = educationRepository.save(education);
            return educationMapper.toDto(updatedEducation);
        });
    }

    @Override
    public void delete(@NonNull UUID id) {
        User user = authService.getLoggedInUser();
        if (educationRepository.findByIdAndUser_Id(id, user.getId()).isPresent()) {
            educationRepository.deleteById(id);
        }
    }

    @Override
    public Page<EducationDTO> filter(@NonNull EducationFilterInput filter, @NonNull Pageable pageable) {
        User user = authService.getLoggedInUser();
        Specification<Education> spec = EducationSpecification.filter(filter, user);
        Page<Education> educations = educationRepository.findAll(spec, pageable);
        return educations.map(educationMapper::toDto);
    }
}
