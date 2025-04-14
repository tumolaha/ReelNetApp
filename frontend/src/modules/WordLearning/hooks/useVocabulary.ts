import { useState, useCallback, useEffect } from 'react';
import { vocabularyApi } from '../api/vocabularyApi';
import { IVocabulary, IVocabularyFilters } from '../types/vocabulary.types';
import { useToast } from '@/shared/hooks/useToast';

export const useVocabulary = (initialFilters?: IVocabularyFilters) => {
  const [vocabularies, setVocabularies] = useState<IVocabulary[]>([]);
  const [selectedVocabulary, setSelectedVocabulary] = useState<IVocabulary | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [filters, setFilters] = useState<IVocabularyFilters>(initialFilters || {});
  
  const { showToast } = useToast();

  const fetchVocabularies = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await vocabularyApi.getVocabularies(filters);
      setVocabularies(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch vocabularies');
      showToast({ type: 'error', message: 'Failed to fetch vocabularies' });
    } finally {
      setLoading(false);
    }
  }, [filters, showToast]);

  const selectVocabulary = useCallback(async (id: string) => {
    try {
      setLoading(true);
      const vocabulary = await vocabularyApi.getVocabularyById(id);
      setSelectedVocabulary(vocabulary);
    } catch (err) {
      showToast({ type: 'error', message: 'Failed to fetch vocabulary details' });
    } finally {
      setLoading(false);
    }
  }, [showToast]);

  const startLearning = useCallback(async (vocabularyId: string) => {
    try {
      await vocabularyApi.startLearning(vocabularyId);
      showToast({ type: 'success', message: 'Started learning session' });
    } catch (err) {
      showToast({ type: 'error', message: 'Failed to start learning session' });
    }
  }, [showToast]);

  const updateFilters = useCallback((newFilters: Partial<IVocabularyFilters>) => {
    setFilters(prev => ({ ...prev, ...newFilters }));
  }, []);

  const rateVocabulary = useCallback(async (vocabularyId: string, rating: number, review?: string) => {
    try {
      await vocabularyApi.rateVocabulary(vocabularyId, rating, review);
      showToast({ type: 'success', message: 'Rating submitted successfully' });
      await fetchVocabularies(); // Refresh list to show updated rating
    } catch (err) {
      showToast({ type: 'error', message: 'Failed to submit rating' });
    }
  }, [fetchVocabularies, showToast]);

  useEffect(() => {
    fetchVocabularies();
  }, [fetchVocabularies]);

  return {
    vocabularies,
    selectedVocabulary,
    loading,
    error,
    filters,
    selectVocabulary,
    updateFilters,
    startLearning,
    rateVocabulary
  };
}; 