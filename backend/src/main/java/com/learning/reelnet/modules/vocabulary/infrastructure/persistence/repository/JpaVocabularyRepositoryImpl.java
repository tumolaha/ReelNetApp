package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularyRepository;

import lombok.AllArgsConstructor;


@Repository
@AllArgsConstructor
public class JpaVocabularyRepositoryImpl implements VocabularyRepository {
    private final SpringDataVocabularyRepository springDataVocabularyRepository;

    /*
     * * Find a Vocabulary by its ID.
     * * @param id UUID representing the ID of the Vocabulary to be found.
     * * @return Vocabulary object that matches the given ID.
     */
    @Override
    public Vocabulary findById(UUID id) {
        // TODO Auto-generated method stub
        return springDataVocabularyRepository.findById(id).orElse(null);
    }

    /*
     * * Find vocabulary by their set IDs.
     * * @param ids Set of UUIDs representing the IDs of the Vocabulary to be
     * found.
     * * @return List of Vocabulary objects that match the given IDs.
     */
    @Override
    public List<Vocabulary> findById(Set<UUID> ids) {
        return springDataVocabularyRepository.findAllById(ids).stream().toList();
    }

    /*
     * * Find all Vocabulary in the database.
     * * @return List of Vocabulary objects that match the given IDs.
     */
    @Override
    public List<Vocabulary> findAll() {
        return springDataVocabularyRepository.findAll().stream().toList();
    }

    /*
     * * Save a Vocabulary to the database.
     * * @param vocabulary Vocabulary object to be saved.
     * * @return Saved Vocabulary object.
     */
    @Override
    public void save(Vocabulary vocabulary) {
        springDataVocabularyRepository.save(vocabulary);
    }

    /*
     * * Delete a Vocabulary by its ID.
     * * @param id UUID representing the ID of the Vocabulary to be deleted.
     */
    @Override
    public void deleteById(UUID id) {
        springDataVocabularyRepository.deleteById(id);
    }

    /*
     * * Find a list of Vocabulary by their IDs.
     * * @param ids List of UUIDs representing the IDs of the Vocabulary to be
     * found.
     * * @return List of Vocabulary objects that match the given IDs.
     */
    @Override
    public List<Vocabulary> findByCriteria(String criteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByCriteria'");
    }

    /*
     * * Find a Vocabulary by its ID.
     * * @param id UUID representing the ID of the Vocabulary to be found.
     * * @return Vocabulary object that matches the given ID.
     */
    @Override
    public Vocabulary getReferenceById(UUID id) {
        return springDataVocabularyRepository.getReferenceById(id);
    }

}
