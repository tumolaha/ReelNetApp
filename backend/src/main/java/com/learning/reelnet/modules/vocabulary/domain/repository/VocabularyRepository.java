package com.learning.reelnet.modules.vocabulary.domain.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;

import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyRepository {

    /*
     * * Find a Vocabulary by its ID.
     * * @param id UUID representing the ID of the Vocabulary to be found.
     * * @return Vocabulary object that matches the given ID.
     */
    Vocabulary findById(UUID id);

    /*
     * * Find a list of Vocabulary by their IDs.
     * * @param ids List of UUIDs representing the IDs of the Vocabulary to be
     * found.
     * * @return List of Vocabulary objects that match the given IDs.
     */
    List<Vocabulary> findById(Set<UUID> ids);

    /*
     * * Find a list of Vocabulary by their IDs.
     * * @param ids List of UUIDs representing the IDs of the Vocabulary to be
     * found.
     * * @return List of Vocabulary objects that match the given IDs.
     */
    List<Vocabulary> findAll();

    /*
     * * Save a Vocabulary to the database.
     * * @param vocabulary Vocabulary object to be saved.
     * * @return Saved Vocabulary object.
     */
    void save(Vocabulary vocabulary);

    /*
     * * Delete a Vocabulary by its ID.
     * * @param id UUID representing the ID of the Vocabulary to be deleted.
     */
    void deleteById(UUID id);

    /*
     * * Find a list of Vocabulary by their IDs.
     * * @param ids List of UUIDs representing the IDs of the Vocabulary to be
     * found.
     * * @return List of Vocabulary objects that match the given IDs.
     */
    List<Vocabulary> findByCriteria(String criteria);

    /*
     * * Find a list of Vocabulary by their IDs.
     * * @param ids List of UUIDs representing the IDs of the Vocabulary to be
     * found.`
     * * @return List of Vocabulary objects that match the given IDs.
     */
    Vocabulary getReferenceById(UUID id);

}
