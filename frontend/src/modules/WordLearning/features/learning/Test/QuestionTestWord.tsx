import AnswerTestWord from "./AnswerTestWord";
import { AnswerOption } from "./ContentTestWord";
import { HiOutlineVolumeUp } from "react-icons/hi";
import { MdFlag } from "react-icons/md";

interface QuestionTestWordIProps {
  selectedAnswer: string | null;
  setSelectedAnswer: (value: string) => void;
  isShowAnswer: boolean;
  setIsShowAnswer: (value: boolean) => void;
}
const QuestionTestWord = ({
  selectedAnswer,
  setSelectedAnswer,
  isShowAnswer,
  setIsShowAnswer,
}: QuestionTestWordIProps) => {
  // Optimized answer options as a constant outside component
  const answerOptions: AnswerOption[] = [
    {
      id: "1",
      text: "expressly",
      explanation:
        "____ nằm sau động từ 'to be' và trước '' và 'stated' - dạng V3/V-ed => đây là cấu trúc bị động nên chỉ có thể là trạng từ",
      isCorrect: false,
    },
    {
      id: "2",
      text: "has proven",
      explanation:
        "for + một khoảng thời gian => Đây là dấu hiệu của thì hoàn thành --> chọn 'has proven'.",
      isCorrect: true,
    },
    {
      id: "3",
      text: "except",
      explanation: "except: ngoại trừ + dạng V3/V-ed",
      isCorrect: false,
    },
    {
      id: "4",
      text: "recent",
      explanation: "recent: gần đây + dạng V3/V-ed ",
      isCorrect: false,
    },
  ];

  // Optimized text rendering with memoized function
  const renderTextShow = () => {
    const answer = answerOptions.find((item) => item.id === selectedAnswer);

    if (isShowAnswer && answer?.isCorrect) {
      return (
        <p className="text-emerald-500 dark:text-emerald-400 text-sm font-bold">
          Chọn định nghĩa đúng.
        </p>
      );
    }

    if (isShowAnswer && !answer?.isCorrect) {
      return (
        <p className="text-rose-500 dark:text-rose-400 text-sm font-bold">
          Đừng lo, bạn vẫn đang học mà!
        </p>
      );
    }

    if (isShowAnswer && !answer) {
      return (
        <p className="text-slate-500 dark:text-slate-400 text-sm font-bold">
          Thử lại câu hỏi này sau!
        </p>
      );
    }

    return (
      <p className="text-slate-500 dark:text-slate-400 text-sm font-bold">
        Chọn định nghĩa đúng
      </p>
    );
  };

  const handleAnswerSelect = (optionId: string) => {
    setSelectedAnswer(optionId);
    setIsShowAnswer(true);
  };

  const handleDontKnow = () => {
    setIsShowAnswer(true);
  };
  return (
    <div className="w-full rounded-md bg-slate-100 dark:bg-slate-800 p-4 shadow-sm">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4 text-slate-700 dark:text-slate-300">
          <h5 className="text-base font-semibold">Câu hỏi 1</h5>
          <button className="hover:text-slate-900 dark:hover:text-slate-100 transition-colors">
            <HiOutlineVolumeUp size={20} />
          </button>
        </div>
        <button className="flex items-center gap-4 text-slate-700 dark:text-slate-300 hover:text-amber-400 dark:hover:text-amber-300 transition-colors">
          <MdFlag size={20} />
        </button>
      </div>

      <div className="mt-4">
        <p className="text-slate-700 dark:text-slate-300">
          What is the capital of France?
        </p>
      </div>

      <div className="flex items-center justify-start mt-8">
        {renderTextShow()}
      </div>

      <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
        {answerOptions.map((option) => (
          <AnswerTestWord
            key={option.id}
            answer={option}
            isShowResult={isShowAnswer}
            onClick={() => handleAnswerSelect(option.id)}
            isSelected={selectedAnswer === option.id}
            index={0}
          />
        ))}
      </div>

      <div className="flex justify-center mt-6">
        <button
          className="text-sky-500 dark:text-sky-400 hover:text-sky-600 dark:hover:text-sky-300 text-sm font-medium transition-colors"
          onClick={handleDontKnow}
        >
          Bạn không biết?
        </button>
      </div>
    </div>
  );
};

export default QuestionTestWord;
