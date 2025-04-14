import { useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import {
  ChevronLeft,
  BookOpen,
  Users,
  Heart,
  Download,
  Share,
  Play,
  Plus,
  Search,
  ArrowUpDown,
  ExternalLink,
  Volume,
  Star,
  ListFilter,
} from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
  CardFooter,
} from "@/shared/components/ui/card";
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from "@/shared/components/ui/tabs";
import { Progress } from "@/shared/components/ui/progress";
import { Input } from "@/shared/components/ui/input";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/shared/components/ui/table";
import {
  Accordion,
  AccordionItem,
  AccordionTrigger,
  AccordionContent,
} from "@/shared/components/ui/accordion";
import { ROUTES } from "@/core/routes/constants";
import { Skeleton } from "@/shared/components/ui/skeleton";
import { useGetVocabularyByCategoryQuery } from "../../api/vocabularyApi";
import { useGetVocabularySetByIdQuery } from "../../api/vocabularySetApi";

// For demo purposes only - would be fetched from API in real app
const vocabularySets = [
  {
    id: "1",
    title: "TOEFL Essential Vocabulary",
    description: "The most common 500 words appearing in TOEFL tests",
    author: "Sarah Johnson",
    wordCount: 500,
    category: "Exam Prep",
    likes: 1245,
    downloads: 3782,
    progress: 35,
    image:
      "https://images.unsplash.com/photo-1546410531-bb4caa6b424d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
    learningCount: 175,
    masteredCount: 75,
    notStartedCount: 250,
    createdAt: "2023-05-12",
    description_long:
      "This comprehensive vocabulary list contains the most frequently tested words in the TOEFL exam. Mastering these words will significantly improve your score and help you communicate more effectively in academic settings. The list is regularly updated based on the latest exam patterns.",
  },
  {
    id: "2",
    title: "Business English Terms",
    description: "Professional vocabulary for workplace communication",
    author: "Michael Chen",
    wordCount: 320,
    category: "Business",
    likes: 879,
    downloads: 2156,
    progress: 68,
    image:
      "https://images.unsplash.com/photo-1507679799987-c73779587ccf?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
    learningCount: 120,
    masteredCount: 98,
    notStartedCount: 102,
    createdAt: "2023-06-24",
    description_long:
      "A carefully curated collection of essential business English vocabulary for professional environments. This list covers terminology used in meetings, presentations, negotiations, and written business communication. Perfect for professionals looking to enhance their workplace communication skills.",
  },
  {
    id: "3",
    title: "Academic Writing Vocabulary",
    description: "Formal expressions for essays and research papers",
    author: "Emma Thompson",
    wordCount: 250,
    category: "Academic",
    likes: 603,
    downloads: 1892,
    progress: 10,
    image:
      "https://images.unsplash.com/photo-1501504905252-473c47e087f8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
    learningCount: 25,
    masteredCount: 0,
    notStartedCount: 225,
    createdAt: "2023-07-15",
    description_long:
      "Enhance your academic writing with this comprehensive collection of formal vocabulary and expressions commonly used in scholarly papers. This vocabulary list will help you express complex ideas more precisely and professionally in your academic writing.",
  },
];

const wordsList = [
  {
    id: 1,
    word: "Abundant",
    pronunciation: "/əˈbʌndənt/",
    partOfSpeech: "adjective",
    meaning: "Existing or available in large quantities",
    example: "The region has abundant natural resources.",
    status: "mastered",
    difficulty: "medium",
  },
  {
    id: 2,
    word: "Adverse",
    pronunciation: "/ˈadvɜːrs/",
    partOfSpeech: "adjective",
    meaning: "Preventing success or development; harmful; unfavorable",
    example: "The adverse weather conditions made travel difficult.",
    status: "learning",
    difficulty: "hard",
  },
  {
    id: 3,
    word: "Aggregate",
    pronunciation: "/ˈæɡrɪɡət/",
    partOfSpeech: "verb",
    meaning: "To form or group into a class or cluster",
    example: "The data was aggregated to give an overall picture.",
    status: "learning",
    difficulty: "hard",
  },
  {
    id: 4,
    word: "Allocate",
    pronunciation: "/ˈæləkeɪt/",
    partOfSpeech: "verb",
    meaning:
      "To distribute according to a plan or set apart for a special purpose",
    example: "The professor allocated 30 minutes for questions.",
    status: "mastered",
    difficulty: "medium",
  },
  {
    id: 5,
    word: "Ambiguous",
    pronunciation: "/æmˈbɪɡjuəs/",
    partOfSpeech: "adjective",
    meaning:
      "Open to more than one interpretation; not having one obvious meaning",
    example: "The instructions were ambiguous and confusing.",
    status: "learning",
    difficulty: "hard",
  },
  {
    id: 6,
    word: "Analogy",
    pronunciation: "/əˈnælədʒi/",
    partOfSpeech: "noun",
    meaning:
      "A comparison between two things, typically for the purpose of explanation or clarification",
    example: "He used an analogy to explain the complex concept.",
    status: "not-started",
    difficulty: "medium",
  },
  {
    id: 7,
    word: "Anomaly",
    pronunciation: "/əˈnɒməli/",
    partOfSpeech: "noun",
    meaning:
      "Something that deviates from what is standard, normal, or expected",
    example:
      "The test results showed an anomaly that required further investigation.",
    status: "not-started",
    difficulty: "hard",
  },
  {
    id: 8,
    word: "Anticipate",
    pronunciation: "/ænˈtɪsɪpeɪt/",
    partOfSpeech: "verb",
    meaning: "To regard as probable; expect or predict",
    example: "They anticipate that the project will be completed by June.",
    status: "mastered",
    difficulty: "medium",
  },
  {
    id: 9,
    word: "Arbitrary",
    pronunciation: "/ˈɑːrbɪtrəri/",
    partOfSpeech: "adjective",
    meaning:
      "Based on random choice or personal whim, rather than any reason or system",
    example: "The decision to choose these specific colors seemed arbitrary.",
    status: "learning",
    difficulty: "hard",
  },
  {
    id: 10,
    word: "Articulate",
    pronunciation: "/ɑːrˈtɪkjuleɪt/",
    partOfSpeech: "verb",
    meaning: "Express (an idea or feeling) fluently and coherently",
    example: "She was able to articulate her concerns clearly and concisely.",
    status: "not-started",
    difficulty: "medium",
  },
  {
    id: 11,
    word: "Assimilate",
    pronunciation: "/əˈsɪməleɪt/",
    partOfSpeech: "verb",
    meaning: "Take in and understand (information, ideas, or culture)",
    example: "It takes time to assimilate so much new information.",
    status: "learning",
    difficulty: "hard",
  },
  {
    id: 12,
    word: "Bias",
    pronunciation: "/ˈbaɪəs/",
    partOfSpeech: "noun",
    meaning: "Prejudice in favor of or against one thing, person, or group",
    example: "The study showed a clear bias toward younger participants.",
    status: "mastered",
    difficulty: "medium",
  },
];

const difficultyFilters = ["All", "Easy", "Medium", "Hard"];
const statusFilters = ["All", "Learning", "Mastered", "Not Started"];

const OverviewVocabularyPackageContainer = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("All");
  const [difficultyFilter, setDifficultyFilter] = useState("All");
  const { data: vocabularySetData, isLoading: isVocabularySetLoading } =
    useGetVocabularySetByIdQuery({ id: id || "" });
  const {
    data: vocabulariesCategoryData,
    isLoading: isVocabulariesCategoryLoading,
  } = useGetVocabularyByCategoryQuery(id || "");

  // Find the vocabulary set based on the ID
  const vocabularySet =
    vocabularySets.find((set) => set.id === id) || vocabularySets[0];

  // Filter words based on search, status, and difficulty
  const filteredWords = wordsList.filter((word) => {
    const matchesSearch =
      word.word.toLowerCase().includes(searchTerm.toLowerCase()) ||
      word.meaning.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus =
      statusFilter === "All" ||
      word.status.toLowerCase() ===
        statusFilter.toLowerCase().replace(" ", "-");
    const matchesDifficulty =
      difficultyFilter === "All" ||
      word.difficulty.toLowerCase() === difficultyFilter.toLowerCase();

    return matchesSearch && matchesStatus && matchesDifficulty;
  });

  // Function to handle tab switching
  const handleTabClick = (tabId: string) => {
    const tabElement = document.querySelector(`[data-value="${tabId}"]`);
    if (tabElement) {
      // Use the click method on HTMLElement
      (tabElement as HTMLElement).click();
    }
  };

  if (isVocabularySetLoading) {
    return (
      <div className="container py-8 max-w-7xl mx-auto">
        {/* Back button skeleton */}
        <div className="mb-8">
          <Skeleton className="h-8 w-40 mb-4" />

          <div className="flex flex-col md:flex-row justify-between gap-4 items-start">
            <div className="flex-1">
              <Skeleton className="h-8 w-3/4 mb-2" />
              <Skeleton className="h-4 w-full mb-4" />
              <div className="flex flex-wrap gap-3 items-center">
                <Skeleton className="h-4 w-24" />
                <Skeleton className="h-4 w-24" />
                <Skeleton className="h-4 w-24" />
              </div>
            </div>
            <div className="flex gap-2 self-start">
              <Skeleton className="h-8 w-24" />
            </div>
          </div>
        </div>

        {/* Tabs skeleton */}
        <div className="w-full">
          <div className="flex gap-2 mb-6">
            <Skeleton className="h-10 w-32" />
          </div>

          {/* Content skeleton */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="md:col-span-2 space-y-6">
              {/* Main content card skeleton */}
              <Card>
                <CardHeader>
                  <Skeleton className="h-6 w-48" />
                </CardHeader>
                <CardContent className="space-y-4">
                  <Skeleton className="h-4 w-full" />
                  <Skeleton className="h-4 w-3/4" />
                  <Skeleton className="h-4 w-1/2" />
                </CardContent>
              </Card>

              {/* Sample words preview skeleton */}
              <Card>
                <CardHeader>
                  <Skeleton className="h-6 w-32" />
                  <Skeleton className="h-4 w-48" />
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {[1, 2, 3].map((i) => (
                      <div key={i} className="space-y-2">
                        <Skeleton className="h-8 w-full" />
                        <Skeleton className="h-4 w-3/4" />
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Sidebar skeleton */}
            <div className="space-y-6">
              {/* Progress card skeleton */}
              <Card>
                <CardHeader>
                  <Skeleton className="h-6 w-32" />
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="text-center mb-4">
                    <Skeleton className="h-8 w-16 mx-auto mb-2" />
                    <Skeleton className="h-4 w-24 mx-auto" />
                  </div>
                  <Skeleton className="h-2 w-full" />
                </CardContent>
              </Card>

              {/* Study options skeleton */}
              <Card>
                <CardHeader>
                  <Skeleton className="h-6 w-32" />
                </CardHeader>
                <CardContent className="space-y-2">
                  {[1, 2, 3, 4].map((i) => (
                    <Skeleton key={i} className="h-10 w-full" />
                  ))}
                </CardContent>
              </Card>

              {/* Related sets skeleton */}
              <Card>
                <CardHeader>
                  <Skeleton className="h-6 w-32" />
                </CardHeader>
                <CardContent className="space-y-3">
                  {[1, 2, 3].map((i) => (
                    <Skeleton key={i} className="h-16 w-full" />
                  ))}
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container py-8 max-w-7xl mx-auto">
      {/* Back button and header */}
      <div className="mb-8">
        <Button variant="ghost" size="sm" className="mb-4" asChild>
          <Link to={ROUTES.VOCABULARIES.COMMUNITY_VOCABULARIES}>
            <ChevronLeft className="h-4 w-4 mr-2" /> Back to Community Lists
          </Link>
        </Button>

        <div className="flex flex-col md:flex-row justify-between gap-4 items-start">
          <div className="flex-1">
            <h1 className="text-3xl font-bold mb-2">
              {vocabularySetData?.data.name}
            </h1>
            {/* <div className="text-muted-foreground mb-4">
              {vocabularySetData?.data.description}
            </div> */}
            <div className="flex flex-wrap gap-3 items-center text-sm text-muted-foreground">
              <div className="flex items-center">
                <Users className="h-4 w-4 mr-1" />
                <span>By {vocabularySetData?.data.createdBy }</span>
              </div>
              <div className="flex items-center">
                <BookOpen className="h-4 w-4 mr-1" />
                <span>{vocabularySetData?.data.itemCount} words</span>
              </div>
              {Array.isArray(vocabularySetData?.data.tags) 
                ? vocabularySetData?.data.tags.map((tag: string) => (
                    <div
                      key={tag}
                      className="inline-block px-2 py-1 text-xs font-medium rounded-full bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400"
                    >
                      {tag}
                    </div>
                  ))
                : vocabularySetData?.data.tags && (
                    <div
                      className="inline-block px-2 py-1 text-xs font-medium rounded-full bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400"
                    >
                      {vocabularySetData?.data.tags}
                    </div>
                  )
              }
              <div className="flex items-center">
                <Heart className="h-4 w-4 mr-1 text-red-500" />
                <span className="mr-2">{vocabularySetData?.data.likeCount}</span>
              </div>
              <div className="flex items-center">
                <Download className="h-4 w-4 mr-1" />
                <span>{vocabularySetData?.data.shareCount}</span>
              </div>
            </div>
          </div>
          <div className="flex gap-2 self-start">
            <Button variant="outline" size="sm">
              <Share className="h-4 w-4 mr-2" /> Share
            </Button>
            <Button
              size="sm"
              onClick={() =>
                navigate(
                  ROUTES.VOCABULARIES.VOCABULARIES_DETAIL.replace(
                    ":id",
                    vocabularySetData?.data.id || ""
                  )
                )
              }
            >
              <Play className="h-4 w-4 mr-2" /> Practice Now
            </Button>
          </div>
        </div>
      </div>

      {/* Tabs for Overview and Words List */}
      <Tabs defaultValue="overview" className="w-full">
        <TabsList className="grid w-full max-w-md grid-cols-2">
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="words">Words List</TabsTrigger>
        </TabsList>

        {/* Overview Tab Content */}
        <TabsContent value="overview" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="md:col-span-2 space-y-6">
              {/* Main content card */}
              <Card>
                <CardHeader>
                  <CardTitle>About This Vocabulary List</CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="prose dark:prose-invert">
                    <p>{vocabularySetData?.data.description}</p>
                  </div>

                  <div className="pt-4">
                    <h4 className="font-medium mb-2">Recommended Study Plan</h4>
                    <ul className="list-disc pl-5 space-y-1 text-sm">
                      <li>Spend 15-20 minutes daily reviewing new words</li>
                      <li>Practice with flashcards for better memorization</li>
                      <li>
                        Use each new word in a sentence to reinforce learning
                      </li>
                      <li>
                        Review previously learned words weekly to prevent
                        forgetting
                      </li>
                    </ul>
                  </div>
                </CardContent>
              </Card>

              {/* Sample words preview */}
              <Card>
                <CardHeader>
                  <CardTitle>Sample Words</CardTitle>
                  <CardDescription>
                    Preview some words from this vocabulary list
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <Accordion type="single" collapsible className="w-full">
                    {vocabulariesCategoryData?.data.content
                      .slice(0, 5)
                      .map((vocabulary) => (
                        <AccordionItem
                          key={vocabulary.id}
                          value={`vocabulary-${vocabulary.id}`}
                        >
                          <AccordionTrigger className="hover:no-underline">
                            <div className="flex justify-between w-full pr-4">
                              <div className="font-medium">
                                {vocabulary?.word}
                              </div>
                              <div className="text-sm text-muted-foreground"></div>
                            </div>
                          </AccordionTrigger>
                          <AccordionContent>
                            <div className="space-y-2">
                              <div className="flex items-center">
                                <div className="text-sm font-medium w-24">
                                  Pronunciation:
                                </div>
                                <div className="text-sm flex items-center">
                                  <Button
                                    variant="ghost"
                                    size="sm"
                                    className="ml-2 h-6 w-6 p-0"
                                  >
                                    <Volume className="h-3 w-3" />
                                  </Button>
                                </div>
                              </div>
                              <div className="flex">
                                <div className="text-sm font-medium w-24">
                                  Meaning:
                                </div>
                                <div className="text-sm"></div>
                              </div>
                              <div className="flex">
                                <div className="text-sm font-medium w-24">
                                  Example:
                                </div>
                                <div className="text-sm italic"></div>
                              </div>
                            </div>
                          </AccordionContent>
                        </AccordionItem>
                      ))}
                  </Accordion>
                </CardContent>
                <CardFooter>
                  <Button
                    variant="outline"
                    className="w-full"
                    onClick={() => handleTabClick("words")}
                  >
                    View All Words
                  </Button>
                </CardFooter>
              </Card>
            </div>

            {/* Sidebar */}
            <div className="space-y-6">
              {/* Progress card */}
              <Card>
                <CardHeader>
                  <CardTitle>Your Progress</CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="text-center mb-4">
                    <div className="text-3xl font-bold">
                      {vocabularySet.progress}%
                    </div>
                    <div className="text-sm text-muted-foreground">
                      Completed
                    </div>
                  </div>
                  <Progress value={vocabularySet.progress} className="h-2" />

                  <div className="grid grid-cols-3 gap-2 pt-4 text-center">
                    <div className="space-y-1">
                      <div className="text-sm font-medium">
                        {vocabularySet.masteredCount}
                      </div>
                      <div className="text-xs text-muted-foreground">
                        Mastered
                      </div>
                    </div>
                    <div className="space-y-1">
                      <div className="text-sm font-medium">
                        {vocabularySet.learningCount}
                      </div>
                      <div className="text-xs text-muted-foreground">
                        Learning
                      </div>
                    </div>
                    <div className="space-y-1">
                      <div className="text-sm font-medium">
                        {vocabularySet.notStartedCount}
                      </div>
                      <div className="text-xs text-muted-foreground">
                        Not Started
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>

              {/* Study options */}
              <Card>
                <CardHeader>
                  <CardTitle>Study Options</CardTitle>
                </CardHeader>
                <CardContent className="space-y-2">
                  <Button className="w-full justify-start">
                    <Play className="h-4 w-4 mr-2" /> Practice All Words
                  </Button>
                  <Button variant="outline" className="w-full justify-start">
                    <Play className="h-4 w-4 mr-2" /> Practice Difficult Words
                  </Button>
                  <Button variant="outline" className="w-full justify-start">
                    <Play className="h-4 w-4 mr-2" /> Spaced Repetition
                  </Button>
                  <Button variant="outline" className="w-full justify-start">
                    <Plus className="h-4 w-4 mr-2" /> Create Flashcards
                  </Button>
                </CardContent>
              </Card>

              {/* Related sets */}
              <Card>
                <CardHeader>
                  <CardTitle>Related Lists</CardTitle>
                </CardHeader>
                <CardContent className="space-y-3">
                  {vocabularySets
                    .filter((set) => set.id !== id)
                    .map((relatedSet) => (
                      <Link
                        key={relatedSet.id}
                        to={`/vocabulary/${relatedSet.id}`}
                        className="block p-3 rounded-lg border hover:bg-muted/50 transition-colors"
                      >
                        <div className="font-medium">{relatedSet.title}</div>
                        <div className="text-sm text-muted-foreground mt-1">
                          {relatedSet.wordCount} words • {relatedSet.downloads}{" "}
                          downloads
                        </div>
                      </Link>
                    ))}
                </CardContent>
              </Card>
            </div>
          </div>
        </TabsContent>

        {/* Words List Tab Content */}
        <TabsContent value="words" className="space-y-6">
          <Card>
            <CardHeader>
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <CardTitle>
                  Words List ({vocabularySet.wordCount} words)
                </CardTitle>
                <div className="flex items-center gap-2">
                  <div className="relative">
                    <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                    <Input
                      type="search"
                      placeholder="Search words..."
                      className="pl-8 w-full md:w-[200px]"
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                  </div>
                  <Button variant="outline" size="icon">
                    <ListFilter className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div className="flex flex-wrap gap-2 mb-4">
                <div className="text-sm font-medium mr-2">Status:</div>
                {statusFilters.map((status) => (
                  <div
                    key={status}
                    className={`px-3 py-1 rounded-full text-sm cursor-pointer ${
                      statusFilter === status
                        ? "bg-primary text-primary-foreground"
                        : "bg-muted hover:bg-muted/80"
                    }`}
                    onClick={() => setStatusFilter(status)}
                  >
                    {status}
                  </div>
                ))}
              </div>

              <div className="flex flex-wrap gap-2 mb-6">
                <div className="text-sm font-medium mr-2">Difficulty:</div>
                {difficultyFilters.map((difficulty) => (
                  <div
                    key={difficulty}
                    className={`px-3 py-1 rounded-full text-sm cursor-pointer ${
                      difficultyFilter === difficulty
                        ? "bg-primary text-primary-foreground"
                        : "bg-muted hover:bg-muted/80"
                    }`}
                    onClick={() => setDifficultyFilter(difficulty)}
                  >
                    {difficulty}
                  </div>
                ))}
              </div>

              <div className="rounded-md border">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead className="w-[180px]">Word</TableHead>
                      <TableHead>Meaning</TableHead>
                      <TableHead className="w-[100px]">Status</TableHead>
                      <TableHead className="w-[100px]">Difficulty</TableHead>
                      <TableHead className="w-[80px]"></TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredWords.map((word) => (
                      <TableRow key={word.id}>
                        <TableCell className="font-medium">
                          <div>{word.word}</div>
                          <div className="text-xs text-muted-foreground">
                            {word.pronunciation}
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="line-clamp-2">{word.meaning}</div>
                        </TableCell>
                        <TableCell>
                          <div
                            className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium
                            ${
                              word.status === "mastered"
                                ? "bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400"
                                : ""
                            }
                            ${
                              word.status === "learning"
                                ? "bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400"
                                : ""
                            }
                            ${
                              word.status === "not-started"
                                ? "bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-400"
                                : ""
                            }
                          `}
                          >
                            {word.status === "not-started"
                              ? "Not Started"
                              : word.status.charAt(0).toUpperCase() +
                                word.status.slice(1)}
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center">
                            {word.difficulty === "easy" && (
                              <div className="flex">
                                <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                                <Star className="h-3 w-3 text-muted" />
                                <Star className="h-3 w-3 text-muted" />
                              </div>
                            )}
                            {word.difficulty === "medium" && (
                              <div className="flex">
                                <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                                <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                                <Star className="h-3 w-3 text-muted" />
                              </div>
                            )}
                            {word.difficulty === "hard" && (
                              <div className="flex">
                                <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                                <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                                <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                              </div>
                            )}
                          </div>
                        </TableCell>
                        <TableCell>
                          <Button
                            onClick={() =>
                              navigate(ROUTES.VOCABULARIES.VOCABULARY_DETAIL)
                            }
                            variant="ghost"
                            size="sm"
                          >
                            Details
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))}
                    {filteredWords.length === 0 && (
                      <TableRow>
                        <TableCell
                          colSpan={5}
                          className="text-center py-8 text-muted-foreground"
                        >
                          No words found matching your filters
                        </TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </div>
            </CardContent>
            <CardFooter className="flex justify-between">
              <div className="text-sm text-muted-foreground">
                Showing {filteredWords.length} of {wordsList.length} words
              </div>
              <div className="flex items-center gap-2">
                <Button variant="outline" size="sm">
                  <ArrowUpDown className="h-4 w-4 mr-2" /> Sort
                </Button>
                <Button variant="outline" size="sm">
                  <ExternalLink className="h-4 w-4 mr-2" /> Export
                </Button>
              </div>
            </CardFooter>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default OverviewVocabularyPackageContainer;
