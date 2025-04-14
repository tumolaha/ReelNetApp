import { useState } from "react";
import { Button } from "flowbite-react";
import QuestionTestWord from "./QuestionTestWord";
import { motion } from "framer-motion";

export interface AnswerOption {
  id: string;
  text: string;
  explanation: string;
  isCorrect: boolean;
}

const ContentTestWord = () => {
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null);
  const [isShowAnswer, setIsShowAnswer] = useState(false);

  return (
    <div className="h-[calc(100vh-60px)] overflow-y-auto p-4 bg-white dark:bg-slate-900">
      <div className="max-w-5xl mx-auto space-y-4">
        <div className="flex items-center w-full justify-between gap-4">
          <span className="flex items-center justify-center p-1 px-2 rounded-md min-w-[50px] border border-gray-500 dark:border-gray-700 bg-gray-400 dark:bg-gray-800 text-gray-800 dark:text-gray-300 text-sm">
            30
          </span>
          <div className="flex-1 flex items-center justify-start h-2 rounded-md relative bg-gray-800 dark:bg-gray-300/10 overflow-hidden">
            <div className="w-[50%] h-2 bg-green-500 "></div>
          </div>
          <span className="flex items-center justify-center p-1 px-2 rounded-md min-w-[50px] border border-gray-500 dark:border-gray-700 bg-gray-400 dark:bg-gray-800 text-gray-800 dark:text-gray-300 text-sm">
            90
          </span>
        </div>
        <QuestionTestWord
          selectedAnswer={selectedAnswer}
          setSelectedAnswer={setSelectedAnswer}
          isShowAnswer={isShowAnswer}
          setIsShowAnswer={setIsShowAnswer}
        />
      </div>
      {isShowAnswer && (
        <motion.div
          initial={{ opacity: 0, y: 100 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="fixed bottom-0 left-0 w-full flex items-center justify-between px-4 md:px-96 py-4 bg-gray-300 dark:bg-black/10 shadow-md"
        >
          <p className="text-gray-800 dark:text-gray-300">
            Nhấp vào câu trả lời đúng hoặc nhấn phím bất kỳ để tiếp tục
          </p>
          <Button color="blue">Tiếp tục</Button>
        </motion.div>
      )}
    </div>
  );
};

export default ContentTestWord;
