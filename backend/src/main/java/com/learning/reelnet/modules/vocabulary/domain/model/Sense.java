package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.RegisterLabel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "vocabulary", "examples", "registerLabels" })
public class Sense extends BaseEntity<UUID> {

    private Vocabulary vocabulary;

    private String definition; // Định nghĩa bằng tiếng Anh

    private String translation; // Nghĩa tiếng Việt

    private List<Example> examples; // Danh sách câu ví dụ

    private Set<RegisterLabel> registerLabels; // Danh sách phong cách

    private String register;

    private String geographicalUsage;

    private String domain;

    private String grammar;
    
} 