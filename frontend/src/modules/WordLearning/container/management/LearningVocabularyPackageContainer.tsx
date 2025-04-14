// components
import OverviewLearningVocabulary from "../../features/learning/overview/Components/OverviewLearningVocabulary";
import ListVocabulariesDefinition from "../../features/learning/overview/Components/ListVocabulariesDefinition";

const LearningVocabularyPackageContainer = () => {
  return (
    <div className="flex flex-col gap-4 px-40">
      {/* overview learning */}
      <OverviewLearningVocabulary />
      {/* content vocabulary detail per word */}
      <ListVocabulariesDefinition />
    </div>
  );
};

export default LearningVocabularyPackageContainer;
