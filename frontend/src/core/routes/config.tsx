import { Suspense } from "react";
import { createBrowserRouter } from "react-router-dom";
import { ROUTES } from "./constants";
import { ProtectedRoute } from "./components/ProtectedRoute";
import BlankLayout from "./layout/BlankLayout";
import HomePage from "@/shared/pages/HomePage";
import { MainLayout } from "./layout/MainLayout";
import {
  WorkspacePage,
  DetailVocabulariesPage,
  LearningVocabulariesPage,
  LoadingFallback,
  ManagementVocabulariesPage,
  OverviewDetailPackageVocabularyPage,
  PlansSelectionPage,
} from "./lazyComponents";
import CreatePackageVocabulariesPage from "@/modules/WordLearning/page/management-vocabularies/CreatePackageVocabulariesPage";
import DetailVocabularyPage from "@/modules/WordLearning/page/management-vocabularies/DetailVocabularyPage";

export const router = createBrowserRouter([
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <BlankLayout />,
        children: [
          {
            path: ROUTES.HOME,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <HomePage />
              </Suspense>
            ),
          },
          {
            path: ROUTES.PLANS_SELECTION,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <PlansSelectionPage />
              </Suspense>
            ),
          },
        ],
      },
      {
        element: <MainLayout />,
        children: [
          {
            path: ROUTES.WORKSPACE.HOME,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <WorkspacePage />
              </Suspense>
            ),
          },
          // vocabularies
          {
            path: ROUTES.VOCABULARIES.CREATE_PACKAGE_VOCABULARIES,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <CreatePackageVocabulariesPage />
              </Suspense>
            ),
          },
          {
            path: ROUTES.VOCABULARIES.MY_VOCABULARIES,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <ManagementVocabulariesPage />
              </Suspense>
            ),
          },
          {
            path: ROUTES.VOCABULARIES.COMMUNITY_VOCABULARIES,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <ManagementVocabulariesPage />
              </Suspense>
            ),
          },
          {
            path: ROUTES.VOCABULARIES.OVERVIEW_DETAIL_PACKAGE_VOCABULARY,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <OverviewDetailPackageVocabularyPage />
              </Suspense>
            ),
          },
          {
            path: ROUTES.VOCABULARIES.VOCABULARIES_DETAIL,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <DetailVocabulariesPage />
              </Suspense>
            ),
          },
          {
            path: ROUTES.VOCABULARIES.VOCABULARY_DETAIL,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <DetailVocabularyPage />
              </Suspense>
            ),
          },
        ],
      },
      {
        element: <BlankLayout />,
        children: [
          {
            path: ROUTES.LEARNING.LEARNING_VOCABULARY,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <LearningVocabulariesPage />
              </Suspense>
            ),
          },
          

        ],
      },
    ],
  },
]);
