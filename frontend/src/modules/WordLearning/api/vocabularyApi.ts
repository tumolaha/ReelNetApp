import { apiSlice } from "@/core/api/apiSlice";
import { ApiListResponse, ApiResponse } from "@/core/types/api.types";
import { VocabularyType } from "../types/vocabulary.types";

const vocabularyApi = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getVocabulary: builder.query<ApiResponse<VocabularyType>, string>({
      query: (id) => ({
        url: `/vocabulary/${id}`,
        method: "GET",
      }),
    }),
    createVocabulary: builder.mutation<
      ApiResponse<VocabularyType>,
      VocabularyType
    >({
      query: (vocabulary) => ({
        url: `/vocabulary`,
        method: "POST",
        body: vocabulary,
      }),
    }),
    updateVocabulary: builder.mutation<
      ApiResponse<VocabularyType>,
      VocabularyType
    >({
      query: (vocabulary) => ({
        url: `/vocabulary/${vocabulary.id}`,
        method: "PUT",
        body: vocabulary,
      }),
    }),
    deleteVocabulary: builder.mutation<ApiResponse<VocabularyType>, string>({
      query: (id) => ({
        url: `/vocabulary/${id}`,
        method: "DELETE",
      }),
    }),
    getVocabularyByCategory: builder.query<
      ApiListResponse<VocabularyType>,
      string
    >({
      query: (category) => ({
        url: `/vocabulary/category/${category}`,
        method: "GET",
      }),
    }),
    getVocabularyByDifficulty: builder.query<
      ApiResponse<VocabularyType[]>,
      string
    >({
      query: (difficulty) => ({
        url: `/vocabulary/difficulty/${difficulty}`,
        method: "GET",
      }),
    }),
  }),
});

export const {
  useGetVocabularyQuery,
  useCreateVocabularyMutation,
  useUpdateVocabularyMutation,
  useDeleteVocabularyMutation,
  useGetVocabularyByCategoryQuery,
  useGetVocabularyByDifficultyQuery,
} = vocabularyApi;
