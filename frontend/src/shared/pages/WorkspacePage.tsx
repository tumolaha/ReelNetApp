import { useState } from "react";
import { Link } from "react-router-dom";
import {
  BookOpen,
  BarChart3,
  Award,
  Clock,
  Calendar,
  GraduationCap,
  BookMarked,
  Target,
  TrendingUp,
  LineChart,
} from "lucide-react";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/shared/components/ui/card";
import { Button } from "@/shared/components/ui/button";
import { Progress } from "@/shared/components/ui/progress";
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "@/shared/components/ui/tabs";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/shared/components/ui/table";
import { ChartContainer } from "@/shared/components/ui/chart";
import {
  AreaChart,
  Area,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from "recharts";

const DashboardPage = () => {
  const [weeklyProgress, setWeeklyProgress] = useState(78);

  // Sample data for charts
  const weeklyActivityData = [
    { day: "Mon", minutes: 45 },
    { day: "Tue", minutes: 35 },
    { day: "Wed", minutes: 60 },
    { day: "Thu", minutes: 90 },
    { day: "Fri", minutes: 30 },
    { day: "Sat", minutes: 120 },
    { day: "Sun", minutes: 45 },
  ];

  const skillProgressData = [
    { name: "Listening", score: 75 },
    { name: "Reading", score: 85 },
    { name: "Writing", score: 60 },
    { name: "Speaking", score: 55 },
    { name: "Grammar", score: 70 },
    { name: "Vocabulary", score: 80 },
  ];

  const recentActivities = [
    {
      id: 1,
      activity: "Completed IELTS Reading Test",
      date: "2 hours ago",
      type: "Test",
      score: "8/10",
    },
    {
      id: 2,
      activity: "Learned 15 new vocabulary words",
      date: "Yesterday",
      type: "Vocabulary",
      score: null,
    },
    {
      id: 3,
      activity: "Practiced speaking exercise",
      date: "2 days ago",
      type: "Speaking",
      score: null,
    },
    {
      id: 4,
      activity: "Grammar quiz on tenses",
      date: "3 days ago",
      type: "Quiz",
      score: "90%",
    },
    {
      id: 5,
      activity: "Listening comprehension test",
      date: "5 days ago",
      type: "Test",
      score: "7/10",
    },
  ];

  const upcomingLessons = [
    {
      id: 1,
      title: "Advanced Conversation Practice",
      date: "Tomorrow, 10:00 AM",
      duration: "45 min",
    },
    {
      id: 2,
      title: "TOEFL Writing Workshop",
      date: "Sep 15, 3:00 PM",
      duration: "90 min",
    },
    {
      id: 3,
      title: "Business English Vocabulary",
      date: "Sep 18, 5:30 PM",
      duration: "60 min",
    },
  ];

  const learningStats = [
    {
      title: "Daily Streak",
      value: "15 days",
      icon: <Calendar className="w-6 h-6" />,
      color:
        "text-green-500 dark:text-green-400 bg-green-100 dark:bg-green-900/30",
    },
    {
      title: "Study Hours",
      value: "42 hours",
      icon: <Clock className="w-6 h-6" />,
      color: "text-blue-500 dark:text-blue-400 bg-blue-100 dark:bg-blue-900/30",
    },
    {
      title: "Words Learned",
      value: "312 words",
      icon: <BookMarked className="w-6 h-6" />,
      color:
        "text-purple-500 dark:text-purple-400 bg-purple-100 dark:bg-purple-900/30",
    },
    {
      title: "Achievements",
      value: "9 earned",
      icon: <Award className="w-6 h-6" />,
      color:
        "text-amber-500 dark:text-amber-400 bg-amber-100 dark:bg-amber-900/30",
    },
  ];

  return (
    <div className="min-h-screen pb-12">
      {/* Header Section */}
      <section className="relative pt-16 pb-8 overflow-hidden">
        <div
          className="absolute inset-0 bg-gradient-to-b from-blue-50 to-transparent dark:from-blue-950/50 dark:to-transparent"
          aria-hidden="true"
        ></div>
        <div className="container max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
            <div>
              <h1 className="text-3xl font-bold mb-2">
                Learning DashboardPage
              </h1>
              <p className="text-muted-foreground">
                Track your progress and manage your learning journey
              </p>
            </div>
            <div className="flex gap-3">
              <Button size="sm" asChild>
                <Link to="/account">
                  <GraduationCap className="mr-2 h-4 w-4" />
                  My Account
                </Link>
              </Button>
              <Button size="sm" variant="default">
                <Target className="mr-2 h-4 w-4" />
                Set New Goals
              </Button>
            </div>
          </div>
        </div>
      </section>

      {/* Main DashboardPage */}
      <div className="container max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <Tabs defaultValue="overview" className="w-full">
          <TabsList className="mb-8">
            <TabsTrigger value="overview">Overview</TabsTrigger>
            <TabsTrigger value="progress">Progress</TabsTrigger>
            <TabsTrigger value="activities">Activities</TabsTrigger>
            <TabsTrigger value="schedule">Schedule</TabsTrigger>
          </TabsList>

          {/* Overview Tab */}
          <TabsContent value="overview" className="space-y-6">
            {/* Weekly Goal Card */}
            <Card>
              <CardHeader className="pb-2">
                <CardTitle>Weekly Learning Goal</CardTitle>
                <CardDescription>
                  You're on track to meet your weekly goal
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="text-sm font-medium">
                      Progress: {weeklyProgress}%
                    </span>
                    <span className="text-sm text-muted-foreground">
                      Target: 8 hours/week
                    </span>
                  </div>
                  <Progress value={weeklyProgress} className="h-2" />
                </div>

                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-6">
                  {learningStats.map((stat, i) => (
                    <div key={i} className="p-4 rounded-lg border bg-card">
                      <div
                        className={`w-12 h-12 rounded-full flex items-center justify-center mb-3 ${stat.color}`}
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

            {/* DashboardPage Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Weekly Activity Chart */}
              <Card>
                <CardHeader>
                  <CardTitle>Weekly Activity</CardTitle>
                  <CardDescription>
                    Your study time in minutes per day
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="h-80">
                    <ChartContainer
                      config={{
                        minutes: {
                          theme: {
                            light: "#3b82f6",
                            dark: "#60a5fa",
                          },
                        },
                      }}
                    >
                      <BarChart data={weeklyActivityData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="day" />
                        <YAxis />
                        <Tooltip />
                        <Bar dataKey="minutes" fill="var(--color-minutes)" />
                      </BarChart>
                    </ChartContainer>
                  </div>
                </CardContent>
              </Card>

              {/* Skills Progress */}
              <Card>
                <CardHeader>
                  <CardTitle>Skills Progress</CardTitle>
                  <CardDescription>
                    Your proficiency in different language skills
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="h-80">
                    <ChartContainer
                      config={{
                        score: {
                          theme: {
                            light: "#8b5cf6",
                            dark: "#a78bfa",
                          },
                        },
                      }}
                    >
                      <AreaChart data={skillProgressData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Area
                          type="monotone"
                          dataKey="score"
                          fill="var(--color-score)"
                          stroke="var(--color-score)"
                        />
                      </AreaChart>
                    </ChartContainer>
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Recent Activity Table */}
            <Card>
              <CardHeader>
                <CardTitle>Recent Activities</CardTitle>
                <CardDescription>
                  Your learning activities from the past week
                </CardDescription>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Activity</TableHead>
                      <TableHead>Type</TableHead>
                      <TableHead>When</TableHead>
                      <TableHead>Result</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {recentActivities.map((activity) => (
                      <TableRow key={activity.id}>
                        <TableCell className="font-medium">
                          {activity.activity}
                        </TableCell>
                        <TableCell>{activity.type}</TableCell>
                        <TableCell>{activity.date}</TableCell>
                        <TableCell>{activity.score || "-"}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </CardContent>
              <CardFooter>
                <Button variant="outline" className="w-full">
                  View All Activities
                </Button>
              </CardFooter>
            </Card>
          </TabsContent>

          {/* Progress Tab */}
          <TabsContent value="progress" className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle>Progress Overview</CardTitle>
                <CardDescription>
                  Detailed analysis of your learning progress
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-8">
                  {skillProgressData.map((skill) => (
                    <div key={skill.name} className="space-y-2">
                      <div className="flex justify-between">
                        <div className="font-medium">{skill.name}</div>
                        <div className="text-sm text-muted-foreground">
                          {skill.score}/100
                        </div>
                      </div>
                      <Progress value={skill.score} className="h-2" />
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Learning Trends</CardTitle>
                <CardDescription>
                  Your progress over the last 3 months
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="h-80">
                  <div className="text-center p-12 text-muted-foreground">
                    <LineChart className="w-12 h-12 mx-auto mb-4" />
                    <p>
                      Detailed progress charts will appear here after you
                      complete more lessons
                    </p>
                    <Button className="mt-4" variant="outline">
                      Start a New Lesson
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Activities Tab */}
          <TabsContent value="activities" className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle>Recommended Activities</CardTitle>
                <CardDescription>
                  Based on your learning goals and progress
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div className="p-4 border rounded-lg bg-card">
                    <div className="flex items-center gap-4">
                      <div className="bg-blue-100 dark:bg-blue-900/30 p-3 rounded-lg">
                        <BookOpen className="w-6 h-6 text-blue-600 dark:text-blue-400" />
                      </div>
                      <div className="flex-1">
                        <h3 className="font-medium">Reading Comprehension</h3>
                        <p className="text-sm text-muted-foreground">
                          Improve your understanding of complex texts
                        </p>
                      </div>
                      <Button>Start</Button>
                    </div>
                  </div>

                  <div className="p-4 border rounded-lg bg-card">
                    <div className="flex items-center gap-4">
                      <div className="bg-purple-100 dark:bg-purple-900/30 p-3 rounded-lg">
                        <BookMarked className="w-6 h-6 text-purple-600 dark:text-purple-400" />
                      </div>
                      <div className="flex-1">
                        <h3 className="font-medium">Vocabulary Challenge</h3>
                        <p className="text-sm text-muted-foreground">
                          Learn 20 new advanced words
                        </p>
                      </div>
                      <Button>Start</Button>
                    </div>
                  </div>

                  <div className="p-4 border rounded-lg bg-card">
                    <div className="flex items-center gap-4">
                      <div className="bg-green-100 dark:bg-green-900/30 p-3 rounded-lg">
                        <BarChart3 className="w-6 h-6 text-green-600 dark:text-green-400" />
                      </div>
                      <div className="flex-1">
                        <h3 className="font-medium">Grammar Quiz</h3>
                        <p className="text-sm text-muted-foreground">
                          Test your knowledge of verb tenses
                        </p>
                      </div>
                      <Button>Start</Button>
                    </div>
                  </div>
                </div>
              </CardContent>
              <CardFooter>
                <Button variant="outline" className="w-full">
                  View All Recommended Activities
                </Button>
              </CardFooter>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Completed Activities</CardTitle>
                <CardDescription>Your learning history</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="text-center p-12 text-muted-foreground">
                  <TrendingUp className="w-12 h-12 mx-auto mb-4" />
                  <p>You'll see your completed activities history here</p>
                </div>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Schedule Tab */}
          <TabsContent value="schedule" className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle>Upcoming Lessons</CardTitle>
                <CardDescription>
                  Your scheduled learning sessions
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {upcomingLessons.map((lesson) => (
                    <div
                      key={lesson.id}
                      className="p-4 border rounded-lg bg-card"
                    >
                      <div className="flex items-center gap-4">
                        <div className="bg-amber-100 dark:bg-amber-900/30 p-3 rounded-lg">
                          <Calendar className="w-6 h-6 text-amber-600 dark:text-amber-400" />
                        </div>
                        <div className="flex-1">
                          <h3 className="font-medium">{lesson.title}</h3>
                          <div className="flex gap-4 text-sm text-muted-foreground">
                            <span>{lesson.date}</span>
                            <span>Duration: {lesson.duration}</span>
                          </div>
                        </div>
                        <Button variant="outline">Join</Button>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
              <CardFooter className="flex justify-between">
                <Button variant="outline">Schedule New Lesson</Button>
                <Button variant="outline">View Calendar</Button>
              </CardFooter>
            </Card>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default DashboardPage;
