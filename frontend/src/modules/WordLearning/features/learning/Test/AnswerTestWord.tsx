import { IoCheckmark } from "react-icons/io5";
import { IoClose } from "react-icons/io5";
import { AnswerOption } from "./ContentTestWord";

interface AnswerTestWordProps {
  answer: AnswerOption;
  isSelected: boolean;
  isShowResult: boolean;
  onClick?: () => void;
  index: number;
}
const AnswerTestWord = ({
  answer,
  isSelected,
  isShowResult,
  onClick,
  index,
}: AnswerTestWordProps) => {
  const getAnswerStyle = () => {
    if (isShowResult && !answer.isCorrect && isSelected) {
      return "border-red-500 dark:border-red-600 bg-red-500/10 border-solid";
    }
    
    if (isShowResult && answer.isCorrect && isSelected)
      return "border-green-500 dark:border-green-600 border-solid";
    if (isShowResult && answer.isCorrect)
      return "border-green-500 dark:border-green-600 border-dashed";
    return "border-gray-300 dark:border-gray-700";
  };

  return (
    <button
      onClick={onClick}
      disabled={isShowResult || isSelected}
      className={`relative transition-all duration-200 cursor-pointer`}
    >
      <div
        className={`
                border rounded-lg p-6 transition-all duration-200
                ${getAnswerStyle()}
              `}
      >
        <div className="flex items-start gap-3">
          <span
            className={`
                  font-medium text-lg
                  ${
                    isShowResult
                      ? answer.isCorrect
                        ? "text-green-400"
                        : isSelected
                        ? "text-red-400"
                        : "text-gray-400"
                      : "text-blue-400"
                  }
                `}
          >
            {answer.id}
          </span>

          <div className="flex-1">
            {/* Show answer text */}
            <h3
              className={`
                    font-medium 
                    ${
                      isShowResult
                        ? answer.isCorrect
                          ? "text-green-400"
                          : isSelected
                          ? "text-red-400"
                          : "text-gray-400"
                        : "text-gray-200"
                    }
                  `}
            >
              {answer.text}
            </h3>

            {/* Show explanation only for correct answer or selected wrong answer */}
            {isShowResult && (
              <div className="space-y-1 mt-3">
                <p
                  key={index}
                  className={`
                            text-sm
                            ${
                              answer.explanation.startsWith("=>")
                                ? "text-blue-400"
                                : answer.explanation.includes(":")
                                ? "text-gray-400 font-medium"
                                : "text-gray-400"
                            }
                          `}
                >
                  {answer.explanation}
                </p>
              </div>
            )}
          </div>

          {/* Show check/cross icon */}
          {isShowResult && (answer.isCorrect || isSelected) && (
            <div
              className={`
                    absolute top-4 right-4
                    ${answer.isCorrect ? "text-green-500" : "text-red-500"}
                  `}
            >
              {answer.isCorrect ? (
                <IoCheckmark className="w-6 h-6" />
              ) : (
                <IoClose className="w-6 h-6" />
              )}
            </div>
          )}
        </div>
      </div>
    </button>
  );
};

export default AnswerTestWord;
