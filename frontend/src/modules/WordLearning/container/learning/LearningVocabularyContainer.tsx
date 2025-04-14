import ContentFlashCardWord from "../../features/learning/FlashCard/ContentFlashCardWord";
import HeaderLearningWord from "../../features/learning/FlashCard/HeaderLearningWord";
import { useParams } from "react-router-dom";
import ContentTestWord from "../../features/learning/Test/ContentTestWord";

const LearningVocabularyContainer = () => {
  const { id } = useParams();
  const RenderContainer = () => {
    if (id === "flash-card") {
      return <ContentFlashCardWord />;
    }
    return <ContentTestWord />;
  };

  return (
    <div className="w-full h-full overflow-hidden">
      <HeaderLearningWord />
      <RenderContainer />
    </div>
  );
};

export default LearningVocabularyContainer;
