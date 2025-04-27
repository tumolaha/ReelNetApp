package com.learning.reelnet.modules.vocabulary.domain.valueobject;

/**
 * Value object representing the category of a VocabularySet.
 * This helps users find vocabulary sets related to specific topics or purposes.
 */
public enum Category {
    GENERAL("Từ vựng tổng quát"),
    ACADEMIC("Học thuật"),
    BUSINESS("Kinh doanh"),
    TECHNOLOGY("Công nghệ"),
    MEDICAL("Y tế"),
    IELTS("Luyện thi IELTS"),
    TOEIC("Luyện thi TOEIC"),
    TOEFL("Luyện thi TOEFL"),
    TRAVEL("Du lịch"),
    FOOD_AND_DRINK("Ẩm thực"),
    SCIENCE("Khoa học"),
    ARTS("Nghệ thuật"),
    SPORTS("Thể thao"),
    ENTERTAINMENT("Giải trí"),
    SOCIAL_MEDIA("Mạng xã hội"),
    CULTURE("Văn hóa"),
    HISTORY("Lịch sử"),
    GEOGRAPHY("Địa lý"),
    LANGUAGE("Ngôn ngữ"),
    PSYCHOLOGY("Tâm lý học"),
    PHILOSOPHY("Triết học"),
    RELIGION("Tôn giáo"),
    POLITICS("Chính trị"),
    TECHNICAL("Kỹ thuật"),
    FINANCE("Tài chính"),
    ENVIRONMENT("Môi trường"),
    EDUCATION("Giáo dục"),
    DAILY_CONVERSATION("Hội thoại hàng ngày"),
    LITERATURE("Văn học"),
    MUSIC("Âm nhạc"),
    OTHER("Khác");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
