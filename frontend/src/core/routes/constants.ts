export const ROUTES = {
  HOME: '/',
  PLANS_SELECTION: '/plans-selection',
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    FORGOT_PASSWORD: '/auth/forgot-password',
  },
  COMMUNITY: {
    COMMUNITY: '/community',
  },
  USER_PROFILE: {
    PROFILE: '/user-profile',
  },
  WORKSPACE: {
    HOME: '/workspace',
    MY_WORKSPACE: '/workspace/my',
    LIBRARY: '/workspace/library',
    STUDY_PLAN: '/workspace/study-plan',
  },
  LEARNING: {
    MY_WORD: '/learning/my-word',
    COMMUNITY_WORD: '/learning/community-word',
    DETAIL_WORD: '/learning/detail-word/:id',
    WORD_LEARNING: '/learning/word-learning',
    TEST_WORD: '/learning/test-word',
    LEARNING_VOCABULARY: '/learning/vocabulary/:id',
  },
  VOCABULARIES: {
    CREATE_PACKAGE_VOCABULARIES: '/vocabularies/create',
    COMMUNITY_VOCABULARIES: '/vocabularies/community',
    OVERVIEW_DETAIL_PACKAGE_VOCABULARY: '/vocabularies/overview/:id',
    MY_VOCABULARIES: '/vocabularies/my',
    VOCABULARIES_DETAIL: '/vocabularies/:id',
    VOCABULARY_CREATE: '/vocabularies/create',
    VOCABULARY_EDIT: '/vocabularies/edit/:id',
    VOCABULARY_DETAIL: '/vocabulary/:id',
    // learning
    VOCABULARIES_LEARNING: '/vocabularies/learning/:id',
    VOCABULARY_PLASH_CARD: '/vocabulary/flash-card',
    VOCABULARIES_TEST_WORD: '/vocabularies/test-word',

  },

 
} as const;

export type AppRoutes = typeof ROUTES; 