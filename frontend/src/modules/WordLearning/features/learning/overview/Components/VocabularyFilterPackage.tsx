import { FiSearch } from "react-icons/fi";

interface VocabularyFilterPackageProps {
  searchTerm: string;
  setSearchTerm: (value: string) => void;
  selectedLevel: string | null;
  setSelectedLevel: (value: string | null) => void;
  selectedTag: string | null;
  setSelectedTag: (value: string | null) => void;
  showFavoritesOnly: boolean;
  setShowFavoritesOnly: (value: boolean) => void;
  resetFilters: () => void;
  allTags: string[];
  sortBy: "word" | "level" | "mastery" | "lastReviewed";
  sortOrder: "asc" | "desc";
  handleSort: (field: "word" | "level" | "mastery" | "lastReviewed") => void;
}

const VocabularyFilterPackage = ({
  searchTerm,
  setSearchTerm,
  selectedLevel,
  setSelectedLevel,
  selectedTag,
  setSelectedTag,
    showFavoritesOnly,
    setShowFavoritesOnly,
    resetFilters,
    allTags,
    sortBy,
    sortOrder,
    handleSort,
  }: VocabularyFilterPackageProps) => {
    return (
      <div>
        <div className="dark:bg-gray-800 bg-white rounded-xl p-4 mb-6 dark:shadow-dark shadow-md transition-colors">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="relative flex-grow">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <FiSearch className="dark:text-gray-400 text-gray-400" />
              </div>
              <input
                type="text"
                className="block w-full pl-10 pr-3 py-2 border dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:placeholder-gray-400 dark:focus:ring-blue-500 dark:focus:border-blue-500 bg-white border-gray-300 text-gray-900 focus:ring-blue-500 focus:border-blue-500 rounded-lg transition-colors"
                placeholder="Search words, meanings, or tags..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
  
            <div className="flex flex-wrap gap-2">
              <select
                className="border rounded-lg px-3 py-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 border-gray-300 focus:ring-blue-500 focus:border-blue-500 transition-colors"
                value={selectedLevel || ""}
                onChange={(e) => setSelectedLevel(e.target.value || null)}
              >
                <option value="">All Levels</option>
                <option value="BEGINNER">Beginner</option>
                <option value="ELEMENTARY">Elementary</option>
                <option value="INTERMEDIATE">Intermediate</option>
                <option value="UPPER_INTERMEDIATE">Upper Intermediate</option>
                <option value="ADVANCED">Advanced</option>
              </select>
  
              <select
                className="border rounded-lg px-3 py-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 border-gray-300 focus:ring-blue-500 focus:border-blue-500 transition-colors"
                value={selectedTag || ""}
                onChange={(e) => setSelectedTag(e.target.value || null)}
              >
                <option value="">All Tags</option>
                {allTags.map((tag) => (
                  <option key={tag} value={tag}>
                    {tag}
                  </option>
                ))}
              </select>
  
              <button
                className={`px-3 py-2 rounded-lg border ${
                  showFavoritesOnly
                    ? "dark:bg-yellow-900 dark:text-yellow-200 dark:border-yellow-700 bg-yellow-100 text-yellow-800 border-yellow-300"
                    : "dark:border-gray-600 dark:text-gray-300 border-gray-300 text-gray-700"
                } transition-colors`}
                onClick={() => setShowFavoritesOnly(!showFavoritesOnly)}
              >
                Favorites
              </button>
  
              <button
                className="px-3 py-2 rounded-lg border dark:border-gray-600 dark:text-gray-300 border-gray-300 text-gray-700 flex items-center transition-colors"
                onClick={resetFilters}
              >
                Reset
              </button>
            </div>
          </div>
        </div>
  
        {/* Sorting Options */}
        <div className="flex flex-wrap gap-2 mb-4">
          <span className="dark:text-gray-400 text-gray-500 self-center mr-2 transition-colors">
            Sort by:
          </span>
          <button
            className={`px-3 py-1 rounded-lg text-sm ${
              sortBy === "word"
                ? "dark:bg-blue-900 dark:text-blue-200 bg-blue-100 text-blue-800"
                : "dark:bg-gray-700 dark:text-gray-300 bg-gray-100 text-gray-800"
            } transition-colors`}
            onClick={() => handleSort("word")}
          >
            Word {sortBy === "word" && (sortOrder === "asc" ? "↑" : "↓")}
          </button>
          <button
            className={`px-3 py-1 rounded-lg text-sm ${
              sortBy === "level"
                ? "dark:bg-blue-900 dark:text-blue-200 bg-blue-100 text-blue-800"
                : "dark:bg-gray-700 dark:text-gray-300 bg-gray-100 text-gray-800"
            } transition-colors`}
            onClick={() => handleSort("level")}
          >
            Level {sortBy === "level" && (sortOrder === "asc" ? "↑" : "↓")}
          </button>
          <button
            className={`px-3 py-1 rounded-lg text-sm ${
              sortBy === "mastery"
                ? "dark:bg-blue-900 dark:text-blue-200 bg-blue-100 text-blue-800"
                : "dark:bg-gray-700 dark:text-gray-300 bg-gray-100 text-gray-800"
            } transition-colors`}
            onClick={() => handleSort("mastery")}
          >
            Mastery {sortBy === "mastery" && (sortOrder === "asc" ? "↑" : "↓")}
          </button>
          <button
            className={`px-3 py-1 rounded-lg text-sm ${
              sortBy === "lastReviewed"
                ? "dark:bg-blue-900 dark:text-blue-200 bg-blue-100 text-blue-800"
                : "dark:bg-gray-700 dark:text-gray-300 bg-gray-100 text-gray-800"
            } transition-colors`}
            onClick={() => handleSort("lastReviewed")}
          >
            Last Reviewed{" "}
            {sortBy === "lastReviewed" && (sortOrder === "asc" ? "↑" : "↓")}
          </button>
        </div>
      </div>
    );
};

export default VocabularyFilterPackage;
