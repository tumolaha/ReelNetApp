package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.PartOfSpeech;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "senses", "examples", "synonyms", "antonyms" })
public class Vocabulary extends BaseEntity<UUID> {

    private String headword;
    private String meaning;
    private String pronunciationUk;
    private String pronunciationUs;
    private PartOfSpeech pos;
    private String createdBy;
    
    @Builder.Default
    private boolean isSystem = false;
    
    @Builder.Default
    private Long viewCount = 0L;
    
    private Integer difficultyScore;
    
    @Builder.Default
    private List<Sense> senses = new ArrayList<>();
    
    @Builder.Default
    private List<Example> examples = new ArrayList<>();
    
    @Builder.Default
    private Set<Synonym> synonyms = new HashSet<>();
    
    @Builder.Default
    private Set<Antonym> antonyms = new HashSet<>();

    // Helper methods
    public void addSense(Sense sense) {
        sense.setVocabulary(this);
        this.senses.add(sense);
    }

    public void addExample(Example example) {
        example.setVocabulary(this);
        this.examples.add(example);
    }

    public void addSynonym(Synonym synonym) {
        synonym.setVocabulary(this);
        this.synonyms.add(synonym);
    }

    public void addAntonym(Antonym antonym) {
        antonym.setVocabulary(this);
        this.antonyms.add(antonym);
    }
}
