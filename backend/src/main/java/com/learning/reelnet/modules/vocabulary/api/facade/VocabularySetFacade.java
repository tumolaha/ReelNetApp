package com.learning.reelnet.modules.vocabulary.api.facade;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;

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
         * Lấy lịch sử bộ từ vựng theo ID người dùng
         * 
         * @param userId      ID của người dùng
         * @param queryParams thông tin phân trang và tìm kiếm
         * @throws Exception nếu có lỗi xảy ra trong quá trình lấy dữ liệu
         * @return danh sách lịch sử bộ từ vựng
         */
        Page<VocabularySetDto> getVocabularySetHistoryByUserId(String userId, QueryParams queryParams,
                        FilterParams filterParams, SearchParams searchParams) throws Exception;
}
