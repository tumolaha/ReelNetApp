import { apiSlice, extractQueryResult } from "@/core/api/apiSlice";
import { VocabularySetType } from "../types/vocabularySet.types";
import { ApiListResponse, ApiResponse } from "@/core/types/api.types";
import { BaseQueryParams } from "@/core/api/queryParams";

// Combined type for all query parameters
export type VocabularyQueryParams = BaseQueryParams;

// API slice
const vocabularySetApi = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    // crud
    createVocabularySet: builder.mutation<
      ApiResponse<VocabularySetType>,
      Partial<VocabularySetType>
    >({
      query: (vocabularySet) => ({
        url: "/vocabulary-sets",
        method: "POST",
        data: vocabularySet,
      }),
      invalidatesTags: ["VocabularySet"],
    }),
    updateVocabularySet: builder.mutation<
      ApiResponse<VocabularySetType>,
      VocabularySetType
    >({
      query: (vocabularySet) => ({
        url: `/vocabulary-sets/${vocabularySet.id}`,
        method: "PUT",
        data: vocabularySet,
      }),
      invalidatesTags: (_result, _error, arg) => [
        { type: "VocabularySet", id: arg.id },
        "VocabularySet",
      ],
    }),
    deleteVocabularySet: builder.mutation<
      ApiResponse<VocabularySetType>,
      { id: string }
    >({
      query: ({ id }) => ({
        url: `/vocabulary-sets/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _error, arg) => [
        { type: "VocabularySet", id: arg.id },
        "VocabularySet",
      ],
    }),
    addVocabularyToSet: builder.mutation<
      ApiResponse<VocabularySetType>,
      { setId: string; vocabularyId: string }
    >({
      query: ({ setId, vocabularyId }) => ({
        url: `/vocabulary-sets/${setId}/vocabularies/${vocabularyId}`,
        method: "POST",
      }),
      invalidatesTags: (_result, _error, arg) => [
        { type: "VocabularySet", id: arg.setId },
        { type: "Vocabulary", id: arg.vocabularyId },
      ],
    }),
    removeVocabularyFromSet: builder.mutation<
      ApiResponse<VocabularySetType>,
      { setId: string; vocabularyId: string }
    >({
      query: ({ setId, vocabularyId }) => ({
        url: `/vocabulary-sets/${setId}/vocabularies/${vocabularyId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _error, arg) => [
        { type: "VocabularySet", id: arg.setId },
        { type: "Vocabulary", id: arg.vocabularyId },
      ],
    }),
    // service
    increaseVocabularySetViewCount: builder.mutation<
      ApiResponse<VocabularySetType>,
      { id: string }
    >({
      query: ({ id }) => ({
        url: `/vocabulary-sets/${id}/view`,
        method: "POST",
      }),
    }),
    // query
    getAllVocabularySets: builder.query<
      ApiListResponse<VocabularySetType>,
      VocabularyQueryParams
    >({
      query: (filters) => {
        return {
          url: `/vocabulary-sets`,
          method: "GET",
          params: filters,
        };
      },
      providesTags: (_result, _error, arg) => {
        const tags: { type: "VocabularySet"; id: string }[] = [
          { type: "VocabularySet", id: "LIST" },
        ];
        if (arg.userId) {
          tags.push({ type: "VocabularySet", id: `USER_${arg.userId}` });
        }
        if (arg.category) {
          tags.push({ type: "VocabularySet", id: `CATEGORY_${arg.category}` });
        }
        return tags;
      },
    }),
    getVocabularySetById: builder.query<
      ApiResponse<VocabularySetType>,
      { id: string }
    >({
      query: ({ id }) => ({
        url: `/vocabulary-sets/${id}`,
        method: "GET",
      }),
      providesTags: (_result, _error, arg) => [
        { type: "VocabularySet", id: arg.id },
      ],
    }),
    getVocabularySetsByUserId: builder.query<
      ApiListResponse<VocabularySetType>,
      VocabularyQueryParams
    >({
      query: (filters) => {
        return {
          url: `/vocabulary-sets/user`,
          method: "GET",
          params: filters,
        };
      },
      providesTags: ["VocabularySet"],
    }),

    getFeaturedVocabularySets: builder.query<
      ApiListResponse<VocabularySetType>,
      void
    >({
      query: () => ({
        url: "/vocabulary-sets/featured",
        method: "GET",
      }),
      providesTags: [{ type: "VocabularySet", id: "FEATURED" }],
    }),
    getVocabularySetsByCategory: builder.query<
      ApiListResponse<VocabularySetType>,
      { category: string }
    >({
      query: ({ category }) => ({
        url: "/vocabulary-sets/category",
        method: "GET",
        params: { category },
      }),
      providesTags: (_result, _error, arg) => [
        { type: "VocabularySet", id: `CATEGORY_${arg.category}` },
      ],
    }),
  }),
});

export const {
  useGetVocabularySetByIdQuery,
  useCreateVocabularySetMutation,
  useUpdateVocabularySetMutation,
  useDeleteVocabularySetMutation,
  useGetAllVocabularySetsQuery,
  useGetVocabularySetsByUserIdQuery,
  useGetFeaturedVocabularySetsQuery,
  useGetVocabularySetsByCategoryQuery,
  useAddVocabularyToSetMutation,
  useRemoveVocabularyFromSetMutation,
  useIncreaseVocabularySetViewCountMutation,
} = vocabularySetApi;

// Custom hooks for simplified data access
export const useVocabularySet = (id: string) => {
  const result = useGetVocabularySetByIdQuery({ id }, { skip: !id });
  return extractQueryResult<VocabularySetType>(result);
};

export const useVocabularySets = (params: VocabularyQueryParams) => {
  const result = useGetAllVocabularySetsQuery(params);
  return extractQueryResult<VocabularySetType[]>(result);
};

export const useFeaturedVocabularySets = () => {
  const result = useGetFeaturedVocabularySetsQuery();
  return extractQueryResult<VocabularySetType[]>(result);
};

export const useUserVocabularySets = (userId: string) => {
  const result = useGetVocabularySetsByUserIdQuery(
    { userId },
    { skip: !userId }
  );
  return extractQueryResult<VocabularySetType[]>(result);
};
