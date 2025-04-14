import { FiPlus } from "react-icons/fi";
import PersonalVocabularyCard, {
  VocabularyWord,
} from "../../features/management/personal/components/PersonalVocabularyCard";
import { useEffect } from "react";
import { useState } from "react";
import VocabularyFilterPackage from "../../features/learning/overview/Components/VocabularyFilterPackage";
import VocabularyDetailsModal from "../../features/learning/overview/Components/VocabularyDetailsModal";

// Mock data for demonstration
const mockVocabularyWords: VocabularyWord[] = [
  {
    id: "1",
    word: "Serendipity",
    meaning:
      "The occurrence and development of events by chance in a happy or beneficial way",
    example:
      "A fortunate stroke of serendipity came my way when I met my business partner at a random event.",
    level: "ADVANCED",
    pronunciation: "/ˌser.ənˈdɪp.ə.ti/",
    isFavorite: true,
    lastReviewed: "2023-03-08",
    mastery: 85,
    tags: ["noun", "positive", "chance"],
  },
  {
    id: "2",
    word: "Eloquent",
    meaning: "Fluent or persuasive in speaking or writing",
    example: "She gave an eloquent speech that moved the entire audience.",
    level: "UPPER_INTERMEDIATE",
    pronunciation: "/ˈel.ə.kwənt/",
    isFavorite: false,
    lastReviewed: "2023-03-05",
    mastery: 70,
    tags: ["adjective", "speech", "communication"],
  },
  {
    id: "3",
    word: "Perseverance",
    meaning:
      "Persistence in doing something despite difficulty or delay in achieving success",
    example:
      "His perseverance was rewarded when he finally passed the exam after three attempts.",
    level: "UPPER_INTERMEDIATE",
    pronunciation: "/ˌpɜː.səˈvɪə.rəns/",
    isFavorite: true,
    lastReviewed: "2023-03-01",
    mastery: 90,
    tags: ["noun", "character", "determination"],
  },
  {
    id: "4",
    word: "Ambiguous",
    meaning:
      "Open to more than one interpretation; not having one obvious meaning",
    example:
      "The message was ambiguous and could be interpreted in different ways.",
    level: "INTERMEDIATE",
    pronunciation: "/æmˈbɪɡ.ju.əs/",
    isFavorite: false,
    lastReviewed: "2023-02-28",
    mastery: 65,
    tags: ["adjective", "meaning", "unclear"],
  },
  {
    id: "5",
    word: "Meticulous",
    meaning: "Showing great attention to detail; very careful and precise",
    example: "He was meticulous in his research, checking every fact twice.",
    level: "UPPER_INTERMEDIATE",
    pronunciation: "/məˈtɪk.jə.ləs/",
    isFavorite: true,
    lastReviewed: "2023-02-25",
    mastery: 75,
    tags: ["adjective", "careful", "detailed"],
  },
  {
    id: "6",
    word: "Procrastinate",
    meaning: "Delay or postpone action; put off doing something",
    example: "I always procrastinate when it comes to doing my taxes.",
    level: "INTERMEDIATE",
    pronunciation: "/prəˈkræs.tɪ.neɪt/",
    isFavorite: false,
    lastReviewed: "2023-02-20",
    mastery: 80,
    tags: ["verb", "delay", "behavior"],
  },
  {
    id: "7",
    word: "Resilient",
    meaning: "Able to withstand or recover quickly from difficult conditions",
    example:
      "Children are often more resilient than adults in the face of trauma.",
    level: "UPPER_INTERMEDIATE",
    pronunciation: "/rɪˈzɪl.i.ənt/",
    isFavorite: true,
    lastReviewed: "2023-02-15",
    mastery: 85,
    tags: ["adjective", "strength", "recovery"],
  },
  {
    id: "8",
    word: "Benevolent",
    meaning: "Well meaning and kindly",
    example: "The benevolent donor gave millions to charity.",
    level: "ADVANCED",
    pronunciation: "/bəˈnev.əl.ənt/",
    isFavorite: false,
    lastReviewed: "2023-02-10",
    mastery: 60,
    tags: ["adjective", "kindness", "character"],
  },
];

const levelOrder = {
  BEGINNER: 1,
  ELEMENTARY: 2,
  INTERMEDIATE: 3,
  UPPER_INTERMEDIATE: 4,
  ADVANCED: 5,
};

const StatusVocabulariesContainer = () => {
  const [words, setWords] = useState<VocabularyWord[]>(mockVocabularyWords);
  const [filteredWords, setFilteredWords] =
    useState<VocabularyWord[]>(mockVocabularyWords);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedLevel, setSelectedLevel] = useState<string | null>(null);
  const [selectedTag, setSelectedTag] = useState<string | null>(null);
  const [showFavoritesOnly, setShowFavoritesOnly] = useState(false);
  const [sortBy, setSortBy] = useState<
    "word" | "level" | "mastery" | "lastReviewed"
  >("word");
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
  const [selectedWord, setSelectedWord] = useState<VocabularyWord | null>(null);

  // Get all unique tags from the vocabulary words
  const allTags = Array.from(new Set(words.flatMap((word) => word.tags)));

  // Filter and sort words when any filter or sort option changes
  useEffect(() => {
    let result = [...words];

    // Apply search filter
    if (searchTerm) {
      result = result.filter(
        (word) =>
          word.word.toLowerCase().includes(searchTerm.toLowerCase()) ||
          word.meaning.toLowerCase().includes(searchTerm.toLowerCase()) ||
          word.tags.some((tag) =>
            tag.toLowerCase().includes(searchTerm.toLowerCase())
          )
      );
    }

    // Apply level filter
    if (selectedLevel) {
      result = result.filter((word) => word.level === selectedLevel);
    }

    // Apply tag filter
    if (selectedTag) {
      result = result.filter((word) => word.tags.includes(selectedTag));
    }

    // Apply favorites filter
    if (showFavoritesOnly) {
      result = result.filter((word) => word.isFavorite);
    }

    // Apply sorting
    result.sort((a, b) => {
      if (sortBy === "word") {
        return sortOrder === "asc"
          ? a.word.localeCompare(b.word)
          : b.word.localeCompare(a.word);
      } else if (sortBy === "level") {
        return sortOrder === "asc"
          ? levelOrder[a.level] - levelOrder[b.level]
          : levelOrder[b.level] - levelOrder[a.level];
      } else if (sortBy === "mastery") {
        return sortOrder === "asc"
          ? a.mastery - b.mastery
          : b.mastery - a.mastery;
      } else {
        // lastReviewed
        return sortOrder === "asc"
          ? new Date(a.lastReviewed).getTime() -
              new Date(b.lastReviewed).getTime()
          : new Date(b.lastReviewed).getTime() -
              new Date(a.lastReviewed).getTime();
      }
    });

    setFilteredWords(result);
  }, [
    words,
    searchTerm,
    selectedLevel,
    selectedTag,
    showFavoritesOnly,
    sortBy,
    sortOrder,
  ]);

  // Toggle favorite status
  const handleFavoriteToggle = (id: string) => {
    setWords(
      words.map((word) =>
        word.id === id ? { ...word, isFavorite: !word.isFavorite } : word
      )
    );
  };

  // Play pronunciation audio (mock function)
  const handlePlayPronunciation = (word: string) => {
    console.log(`Playing pronunciation for: ${word}`);
    // In a real app, this would trigger audio playback
  };

  // Handle tag click
  const handleTagClick = (tag: string) => {
    setSelectedTag(tag);
  };

  // Toggle sort order or change sort field
  const handleSort = (field: "word" | "level" | "mastery" | "lastReviewed") => {
    if (sortBy === field) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortBy(field);
      setSortOrder("asc");
    }
  };

  // Reset all filters
  const resetFilters = () => {
    setSearchTerm("");
    setSelectedLevel(null);
    setSelectedTag(null);
    setShowFavoritesOnly(false);
    setSortBy("word");
    setSortOrder("asc");
  };

  // View word details
  const handleCardClick = (word: VocabularyWord) => {
    setSelectedWord(word);
  };

  // Close word details modal
  const closeWordDetails = () => {
    setSelectedWord(null);
  };

  return (
    <div className="dark:bg-gray-900 bg-gray-50 dark:text-white text-gray-900 min-h-screen transition-colors duration-200">
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
          <h1 className="text-3xl font-bold dark:text-white text-gray-800 mb-4 md:mb-0">
            My Vocabulary
            <span className="text-sm font-normal dark:text-gray-400 text-gray-500 ml-2">
              ({filteredWords.length} words)
            </span>
          </h1>
          <div className="flex items-center space-x-4">
            <button className="dark:bg-blue-600 dark:hover:bg-blue-700 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center transition-colors">
              <FiPlus className="mr-2" />
              Add New Word
            </button>
          </div>
        </div>

        <VocabularyFilterPackage
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          selectedLevel={selectedLevel}
          setSelectedLevel={setSelectedLevel}
          selectedTag={selectedTag}
          setSelectedTag={setSelectedTag}
          showFavoritesOnly={showFavoritesOnly}
          setShowFavoritesOnly={setShowFavoritesOnly}
          resetFilters={resetFilters}
          allTags={allTags}
          sortBy={sortBy}
          sortOrder={sortOrder}
          handleSort={handleSort}
        />

        {/* Vocabulary List */}
        {filteredWords.length === 0 ? (
          // No words found matching your filters.
          <div className="dark:bg-gray-800 bg-white rounded-xl dark:shadow-dark shadow-md p-8 text-center transition-colors">
            <p className="dark:text-gray-400 text-gray-500 text-lg transition-colors">
              No words found matching your filters.
            </p>
            <button
              className="mt-4 dark:text-blue-400 dark:hover:text-blue-300 text-blue-600 hover:text-blue-800 transition-colors"
              onClick={resetFilters}
            >
              Clear all filters
            </button>
          </div>
        ) : (
          // Show the list of words
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {filteredWords.map((word) => (
              <PersonalVocabularyCard
                key={word.id}
                word={word}
                onFavoriteToggle={handleFavoriteToggle}
                onPlayPronunciation={handlePlayPronunciation}
                onTagClick={handleTagClick}
                onCardClick={handleCardClick}
              />
            ))}
          </div>
        )}

        {/* Word Details Modal */}
        {selectedWord && (
          <VocabularyDetailsModal
            word={selectedWord}
            onClose={closeWordDetails}
            onFavoriteToggle={handleFavoriteToggle}
            onPlayPronunciation={handlePlayPronunciation}
          />
        )}
      </div>
    </div>
  );
};

export default StatusVocabulariesContainer;



