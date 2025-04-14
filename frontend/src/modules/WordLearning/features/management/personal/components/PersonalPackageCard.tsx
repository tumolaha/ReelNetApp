import React from "react";
import { Badge } from "@/shared/components/ui/badge";
import { Button } from "@/shared/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/shared/components/ui/card";
import { BookOpen, Star } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { ROUTES } from "@/core/routes/constants";
import { Progress } from "@/shared/components/ui/progress";
import { PackageVocabulary } from "../../community/types/PackageVocabulary";



interface PersonalPackageCardProps {
  set: PackageVocabulary;
}

const PersonalPackageCard: React.FC<PersonalPackageCardProps> = ({
  set,
}) => {
  const navigate = useNavigate();
  
  return (
    <Card key={set.id} className="overflow-hidden">
      <CardHeader className="pb-2">
        <div className="flex justify-between items-start">
          <Badge className="mb-2">{set.category}</Badge>
          <Button
            variant="ghost"
            size="icon"
            className={
              set.isFavorite ? "text-amber-500" : "text-muted-foreground"
            }
          >
            <Star className="h-[1.2rem] w-[1.2rem]" />
          </Button>
        </div>
        <CardTitle className="line-clamp-1">{set.title}</CardTitle>
        <CardDescription>
          by {set.author} • {set.wordCount} words
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="space-y-2">
          <div className="flex justify-between text-sm">
            <span>Progress</span>
            <span>{set.progress}%</span>
          </div>
          <Progress value={set.progress} className="h-2" />
          <p className="text-sm text-muted-foreground">
              Last studied: {set.lastAccessed}
          </p>
        </div>
      </CardContent>
      <CardFooter className="flex gap-2">
        <Button variant="outline" className="flex-1" asChild>
          <Link
            to={ROUTES.VOCABULARIES.OVERVIEW_DETAIL_PACKAGE_VOCABULARY.replace(
              ":id",
              set.id.toString()
            )}
          >
            <BookOpen className="mr-2 h-4 w-4" />
            View
          </Link>
        </Button>
        <Button
          className="flex-1"
          onClick={() =>
            navigate(
              ROUTES.VOCABULARIES.VOCABULARIES_DETAIL.replace(
                ":id",
                set.id.toString()
              )
            )
          }
        >
          Continue
        </Button>
      </CardFooter>
    </Card>
  );
};

export default PersonalPackageCard;
