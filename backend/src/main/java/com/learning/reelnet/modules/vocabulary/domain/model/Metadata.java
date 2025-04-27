package com.learning.reelnet.modules.vocabulary.domain.model;


import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Metadata extends BaseEntity<UUID> {

    private Vocabulary vocabulary;

    private String frequency; // Mức độ phổ biến (A1, B2, C1, etc.)

    private String region; // Vùng miền sử dụng (US, UK, AU...)

    private String domain; // Chuyên ngành (IT, medical, legal...)
} 