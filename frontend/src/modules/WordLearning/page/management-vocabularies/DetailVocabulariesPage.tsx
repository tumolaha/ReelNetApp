import DropdownCustomContent from "@/shared/components/DropdownCustomContent";
import { Button } from "flowbite-react";
import {
  HiOutlineBookmark,
  HiOutlineDotsHorizontal,
  HiOutlineShare,
} from "react-icons/hi";
import { IoArrowBack } from "react-icons/io5";
import { useNavigate } from "react-router-dom";
import LearningVocabularyPackageContainer from "../../container/management/LearningVocabularyPackageContainer";
const DetailVocabulariesPage = () => {
  return (
    <div className="flex flex-col w-full gap-4">
      {/* seo header */}
      <HeaderDetailVocabularies />
      {/* content */}
      <LearningVocabularyPackageContainer />
    </div>
  );
};

export default DetailVocabulariesPage;

// seo header detail vocabularies
const HeaderDetailVocabularies = () => {
  const navigate = useNavigate();
  return (
    <div className="flex items-center justify-between">
      <div className="flex items-center gap-2">
        <Button
          color="gray"
          onClick={() => navigate(-1)}
          className="p-0"
          size="sm"
        >
          <IoArrowBack size={20} />
        </Button>
        <span className="text-xl font-medium text-gray-500 dark:text-gray-400">
          TOEIC: Intermediate Personal Qualities Vocabulary Set 1
        </span>
      </div>
      <div className="flex items-center gap-2">
        <Button color="gray" onClick={() => {}} className="p-0" size="sm">
          <HiOutlineBookmark size={20} />
          <span className="ml-2 text-sm">Lưu</span>
        </Button>
        <Button color="gray" onClick={() => {}} className="p-0" size="sm">
          <HiOutlineShare size={20} />
        </Button>
        <DropdownCustomContent
          trigger={
            <Button color="gray" onClick={() => {}} className="p-0" size="sm">
              <HiOutlineDotsHorizontal size={20} />
            </Button>
          }
          items={[
            {
              label: "Chia sẻ",
              icon: <HiOutlineShare size={20} />,
              onClick: () => {},
            },
          ]}
        />
      </div>
    </div>
  );
};

