package com.learning.reelnet.modules.vocabulary.api.facade;

import java.util.Optional;
import java.util.UUID;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Category;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.DifficultyLevel;

/**
 * Facade for vocabulary set operations
 */
public interface VocabularySetFacade {
        /**
         * Tạo mới bộ từ vựng
         * 
         * @throws Exception
         */
        Optional<VocabularySetDto> createVocabularySet(VocabularySetDto.CreateRequest createRequest) throws Exception;

        /**
         * Lấy thông tin bộ từ vựng theo ID
         * 
         * @throws Exception
         */
        Optional<VocabularySetDto> getVocabularySetById(UUID id) throws Exception;

        /**
         * Cập nhật bộ từ vựng
         * 
         * @throws Exception
         */
        Optional<VocabularySetDto> updateVocabularySet(UUID id, VocabularySetDto.UpdateRequest updateRequest)
                        throws Exception;

        /**
         * Xóa bộ từ vựng
         * 
         * @throws Exception
         */
        Boolean deleteVocabularySet(UUID id) throws Exception;

        /**
         * Tìm kiếm bộ từ vựng
         */
        Page<VocabularySetDto> searchVocabularySets(
                        FilterParams filterParams,
                        QueryParams queryParams,
                        SearchParams searchParams) throws Exception;

        /**
         * Lấy danh sách bộ từ vựng của người dùng
         */
        Page<VocabularySetDto> getMyVocabularySets(Pageable pageable);

        /**
         * Lấy danh sách bộ từ vựng công khai
         */
        Page<VocabularySetDto> getPublicVocabularySets(Pageable pageable);

        /**
         * Thêm từ vựng vào bộ từ vựng
         */
        boolean addVocabularyToSet(UUID setId, VocabularySetDto.AddVocabularyRequest request);

        /**
         * Xóa từ vựng khỏi bộ từ vựng
         */
        boolean removeVocabularyFromSet(UUID setId, UUID vocabularyId);

        /**
         * Lấy danh sách từ vựng trong bộ từ vựng
         */
        Page<VocabularySetDto.VocabularyItemDto> getVocabulariesInSet(UUID setId, Pageable pageable);

        /**
         * Tăng lượt xem cho bộ từ vựng
         */
        boolean incrementViewCount(UUID id);

        /**
         * Thêm/bỏ thích bộ từ vựng
         */
        boolean toggleLike(UUID id);

        /**
         * Kiểm tra người dùng đã thích bộ từ vựng chưa
         */
        boolean isLikedByUser(UUID id);

        /**
         * Lấy danh sách bộ từ vựng theo danh mục
         */
        Page<VocabularySetDto> getVocabularySetsByCategory(Category category, Pageable pageable);

        /**
         * Lấy danh sách bộ từ vựng theo mức độ khó
         */
        Page<VocabularySetDto> getVocabularySetsByDifficulty(DifficultyLevel difficultyLevel, Pageable pageable);

        /**
         * Lấy danh sách bộ từ vựng theo độ phổ biến
         */
        Page<VocabularySetDto> getVocabularySetsByPopularity(Pageable pageable);
}
