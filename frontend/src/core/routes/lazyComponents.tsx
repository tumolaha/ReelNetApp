import { lazy } from "react";
import LoadingFallbackPage from "@/shared/pages/LoadingFallbackPage";

// Export LoadingFallback component
export const LoadingFallback = () => <LoadingFallbackPage />;

// Lazy load components
export const WorkspacePage = lazy(() => import("@/shared/pages/WorkspacePage"));
export const CommunityPage = lazy(() => import("@/shared/pages/CommunityPage"));
export const ManagementVocabulariesPage = lazy(
  () => import("@/modules/WordLearning/page/management-vocabularies/ManagementVocabulariesPage")
);
export const DetailVocabulariesPage = lazy(
  () => import("@/modules/WordLearning/page/management-vocabularies/DetailVocabulariesPage")
);
export const LearningVocabulariesPage = lazy(
  () => import("@/modules/WordLearning/page/learning/LearningVocabulariesPage")
);

export const OverviewDetailPackageVocabularyPage = lazy(
  () => import("@/modules/WordLearning/page/management-vocabularies/OverviewDetailPackageVocabularyPage")
);
export const PlansSelectionPage = lazy(
  () => import("@/shared/pages/PlansSelectionPage")
);

