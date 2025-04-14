import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  BookMarked,
  Clock,
  Plus,
  Search,
  ListFilter,
  Check,
  ArrowUpDown,
  ExternalLink,
  Folder,
} from "lucide-react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/shared/components/ui/card";
import { Button } from "@/shared/components/ui/button";
import { Input } from "@/shared/components/ui/input";
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "@/shared/components/ui/tabs";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shared/components/ui/dropdown-menu";
import { ROUTES } from "@/core/routes/constants";
import PersonalPackageCard from "../../features/management/personal/components/PersonalPackageCard";
import { PackageVocabulary } from "../../features/management/community/types/PackageVocabulary";

// Mock data for saved vocabulary sets
const savedVocabularySets: PackageVocabulary[] = [
  {
    id: "1",
    title: "TOEFL Essential Vocabulary",
    category: "Exam Preparation",
    wordCount: 120,
    progress: 65,
    lastAccessed: "2 days ago",
    author: "Official TOEFL",
    isFavorite: true,
    description: "TOEFL essential vocabulary",
    likes: 10,
    downloads: 10,
    image: "https://via.placeholder.com/150",
    createdAt: "2021-01-01",
  },
  {
    id: "2",
    title: "Business English",
    category: "Professional",
    wordCount: 85,
    progress: 42,
    lastAccessed: "Yesterday",
    author: "Cambridge English",
    isFavorite: true,
    description: "Business English vocabulary",
    likes: 10,
    downloads: 10,
    image: "https://via.placeholder.com/150",
    createdAt: "2021-01-01",
  },
  {
    id: "3",
    title: "Academic Writing Vocabulary",
    category: "Academic",
    wordCount: 150,
    progress: 28,
    lastAccessed: "4 days ago",
    author: "Oxford Learning",
    isFavorite: false,
    description: "Academic writing vocabulary",
    likes: 10,
    downloads: 10,
    image: "https://via.placeholder.com/150",
    createdAt: "2021-01-01",
  },
  {
    id: "4",
    title: "Daily Conversation",
    category: "Conversation",
    wordCount: 75,
    progress: 90,
    lastAccessed: "Today",
    author: "EF English",
    isFavorite: false,
    description: "Daily conversation vocabulary",
    likes: 10,
    downloads: 10,
    image: "https://via.placeholder.com/150",
    createdAt: "2021-01-01",
  },
  {
    id: "5",
    title: "Travel Vocabulary",
    category: "Travel",
    wordCount: 60,
    progress: 80,
    lastAccessed: "5 days ago",
    author: "Duolingo",
    isFavorite: true,
    description: "Travel vocabulary",
    likes: 10,
    downloads: 10,
    image: "https://via.placeholder.com/150",
    createdAt: "2021-01-01",
  },
];

// Mock data for recently learned words
const recentlyLearnedWords: PackageVocabulary[] = [
  {
    id: "1",
    title: "Meticulous",
    description: "Showing great attention to detail",
    author: "John Doe",
    wordCount: 10,
    likes: 10,
    downloads: 10,
    category: "General",
    image: "https://via.placeholder.com/150",
    progress: 10,
    createdAt: "2021-01-01",
    isFavorite: true,
    lastAccessed: "2021-01-01",
  },
  {
    id: "2",
    title: "Ephemeral",
    description: "Lasting for a very short time",
    author: "John Doe",
    wordCount: 10,
    likes: 10,
    downloads: 10,
    category: "General",
    image: "https://via.placeholder.com/150",
    progress: 10,
    createdAt: "2021-01-01",
    isFavorite: true,
    lastAccessed: "2021-01-01",
  },
  {
    id: "3",
    title: "Ubiquitous",
    description: "Present, appearing, or found everywhere",
    author: "John Doe",
    wordCount: 10,
    likes: 10,
    downloads: 10,
    category: "General",
    image: "https://via.placeholder.com/150",
    progress: 10,
    createdAt: "2021-01-01",
    isFavorite: true,
    lastAccessed: "2021-01-01",
  },
  {
    id: "4",
    title: "Pragmatic",
    description: "Dealing with things sensibly and realistically",
    author: "John Doe",
    wordCount: 10,
    likes: 10,
    downloads: 10,
    category: "General",
    image: "https://via.placeholder.com/150",
    progress: 10,
    createdAt: "2021-01-01",
    isFavorite: true,
    lastAccessed: "2021-01-01",
  },
  {
    id: "5",
    title: "Ambiguous",
    description: "Open to more than one interpretation",
    author: "John Doe",
    wordCount: 10,
    likes: 10,
    downloads: 10,
    category: "General",
    image: "https://via.placeholder.com/150",
    progress: 10,
    createdAt: "2021-01-01",
    isFavorite: true,
    lastAccessed: "2021-01-01",
  },
];

// Mock data for learning statistics
const learningStats = [
  {
    title: "Words Mastered",
    value: 326,
    icon: <Check className="h-5 w-5" />,
    color:
      "text-green-500 dark:text-green-400 bg-green-100 dark:bg-green-900/30",
  },
  {
    title: "Currently Learning",
    value: 84,
    icon: <Clock className="h-5 w-5" />,
    color: "text-blue-500 dark:text-blue-400 bg-blue-100 dark:bg-blue-900/30",
  },
  {
    title: "Vocabulary Sets",
    value: 12,
    icon: <Folder className="h-5 w-5" />,
    color:
      "text-purple-500 dark:text-purple-400 bg-purple-100 dark:bg-purple-900/30",
  },
  {
    title: "Saved Words",
    value: 410,
    icon: <BookMarked className="h-5 w-5" />,
    color:
      "text-amber-500 dark:text-amber-400 bg-amber-100 dark:bg-amber-900/30",
  },
];

const PersonalVocabulary = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [filterCategory, setFilterCategory] = useState("All");
  const [sortOption, setSortOption] = useState("progress");

  // Filter and sort vocabulary sets
  const filteredVocabularySets = savedVocabularySets
    .filter((set) => {
      const matchesSearch = set.title
        .toLowerCase()
        .includes(searchTerm.toLowerCase());
      const matchesCategory =
        filterCategory === "All" || set.category === filterCategory;
      return matchesSearch && matchesCategory;
    })
    .sort((a, b) => {
      if (sortOption === "progress") {
        return b.progress - a.progress;
      } else if (sortOption === "recent") {
        return a.lastAccessed === "Today"
          ? -1
          : b.lastAccessed === "Today"
          ? 1
          : 0;
      } else if (sortOption === "words") {
        return b.wordCount - a.wordCount;
      }
      return 0;
    });

  // Get unique categories for filter
  const categories = [
    "All",
    ...new Set(savedVocabularySets.map((set) => set.category)),
  ];

  return (
    <div className="">
      {/* Header Section */}
      <section className="relative pt-16 pb-8 ">
        <div
          className="absolute inset-0 bg-gradient-to-b from-purple-50 to-transparent dark:from-purple-950/50 dark:to-transparent"
          aria-hidden="true"
        ></div>
        <div className="container max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
            <div>
              <h1 className="text-3xl font-bold mb-2">My Vocabulary</h1>
              <p className="text-muted-foreground">
                Manage your vocabulary sets and track your learning progress
              </p>
            </div>
            <div className="flex gap-3">
              <Button variant="outline" size="sm" asChild>
                <Link to={ROUTES.VOCABULARIES.COMMUNITY_VOCABULARIES}>
                  <ExternalLink className="mr-2 h-4 w-4" />
                  Community Lists
                </Link>
              </Button>
              <Button
                size="sm"
                onClick={() =>
                  navigate(ROUTES.VOCABULARIES.CREATE_PACKAGE_VOCABULARIES)
                }
              >
                <Plus className="mr-2 h-4 w-4" />
                Create New Set
              </Button>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content */}
      <div className="container max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Learning Stats */}
        <Card className="mb-8">
          <CardHeader className="pb-2">
            <CardTitle>Learning Overview</CardTitle>
            <CardDescription>
              Your vocabulary learning statistics
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {learningStats.map((stat, i) => (
                <div key={i} className="p-4 rounded-lg border bg-card">
                  <div
                    className={`w-10 h-10 rounded-full flex items-center justify-center mb-3 ${stat.color}`}
                  >
                    {stat.icon}
                  </div>
                  <h3 className="text-sm font-medium text-muted-foreground">
                    {stat.title}
                  </h3>
                  <p className="text-xl font-semibold">{stat.value}</p>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Tabs defaultValue="sets" className="w-full">
          <TabsList className="mb-6">
            <TabsTrigger value="sets">Vocabulary Sets</TabsTrigger>
            <TabsTrigger value="recent">Recently Learned</TabsTrigger>
            <TabsTrigger value="favorites">Favorites</TabsTrigger>
          </TabsList>

          {/* Vocabulary Sets Tab */}
          <TabsContent value="sets">
            <div className="flex flex-col sm:flex-row gap-4 mb-6">
              {/* Search Bar */}
              <div className="relative w-full sm:w-72">
                <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Search vocabulary sets..."
                  className="pl-8"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>

              {/* Category Filter */}
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="outline" className="w-full sm:w-auto">
                    <ListFilter className="mr-2 h-4 w-4" />
                    {filterCategory === "All"
                      ? "All Categories"
                      : filterCategory}
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  {categories.map((category, index) => (
                    <DropdownMenuItem
                      key={index}
                      onClick={() => setFilterCategory(category)}
                    >
                      {category}
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>

              {/* Sort Options */}
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="outline" className="w-full sm:w-auto">
                    <ArrowUpDown className="mr-2 h-4 w-4" />
                    Sort by:{" "}
                    {sortOption === "progress"
                      ? "Progress"
                      : sortOption === "recent"
                      ? "Recently Used"
                      : "Word Count"}
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuItem onClick={() => setSortOption("progress")}>
                    Progress
                  </DropdownMenuItem>
                  <DropdownMenuItem onClick={() => setSortOption("recent")}>
                    Recently Used
                  </DropdownMenuItem>
                  <DropdownMenuItem onClick={() => setSortOption("words")}>
                    Word Count
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </div>

            {filteredVocabularySets.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {filteredVocabularySets.map((set) => (
                  <PersonalPackageCard key={set.id} set={set} />
                ))}
              </div>
            ) : (
              <div className="text-center py-12">
                <BookMarked className="mx-auto h-12 w-12 text-muted-foreground mb-4" />
                <h3 className="text-xl font-medium mb-2">
                  No vocabulary sets found
                </h3>
                <p className="text-muted-foreground mb-6">
                  Try changing your search or filter criteria
                </p>
                <Button>Browse Community Lists</Button>
              </div>
            )}
          </TabsContent>

          {/* Recently Learned Words Tab */}
          <TabsContent value="recent">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {recentlyLearnedWords.map((set) => (
                <PersonalPackageCard key={set.id} set={set} />
              ))}
            </div>
          </TabsContent>

          {/* Favorites Tab */}
          <TabsContent value="favorites">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {savedVocabularySets
                .filter((set) => set.isFavorite)
                .map((set) => (
                  <PersonalPackageCard key={set.id} set={set} />
                ))}
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default PersonalVocabulary;
