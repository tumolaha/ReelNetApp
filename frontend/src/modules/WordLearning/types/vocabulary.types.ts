export type VocabularyType = {
  id: string; // Changed from string to UUID
  word: string;
  meaning: string;
  example: string;
  rating: number;
  reviewCount: number;
  currentLearners: number;
  createdAt: string;
  updatedAt: string;
  author: AuthorType;
  terms: VocabularyTermType[];
  category: VocabularyCategory; // Changed to VocabularyCategoryType
  difficulty: VocabularyDifficulty;
  status: VocabularyStatus;
}

export type AuthorType = {
  id: string; // Changed from string to UUID
  name: string;
  avatar: string;
  verified: boolean;
}

export type VocabularyTermType = {
  id: string; // Changed from string to UUID
  term: string;
  definition: string;
  example?: string;
  pronunciation?: string;
  imageUrl?: string;
}

export type VocabularyCategory = 'TOEIC' | 'COMMUNITY' | 'BUSINESS' | 'ACADEMIC'; // Consider changing to VocabularyCategoryType
export type VocabularyDifficulty = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type VocabularyStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

export type VocabularyStateType = {
  vocabularies: VocabularyType[];
  selectedVocabulary: VocabularyType | null;
  loading: boolean;
  error: string | null;
}

export type VocabularyFiltersType = {
  category?: VocabularyCategory; // Consider changing to VocabularyCategoryType
  difficulty?: VocabularyDifficulty;
  search?: string;
  sortBy?: 'rating' | 'currentLearners' | 'createdAt';
  order?: 'asc' | 'desc';
} 