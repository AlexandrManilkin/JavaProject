package ru.croc.project.tests;

public class CompletedTestContainer {
    private int id;
    private final Test test;
    private final int[] userAnswers;
    private final int exerciseCount;

    private final int trueAnswersCount;

    public CompletedTestContainer(Test test, int[] userAnswers){
        //TODO сравнить размер массива ответов с количеством заданий
        this.test = test;
        this.userAnswers = userAnswers;
        exerciseCount = test.getExercises().length;
        int trueAnswersCounter = 0;
        for (int i = 0; i < exerciseCount; i++) {
            if(test.getExercises()[i].getTrueAnswerId() == userAnswers[i]){
                trueAnswersCounter++;
            }
        }
        trueAnswersCount = trueAnswersCounter;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public Test getTest(){
        return test;
    }

    public int getTrueAnswersCount(){
        return trueAnswersCount;
    }
    public int[] getUserAnswers(){
        return userAnswers;
    }

    public String toStringWithUserAnswers(){
        var builder = new StringBuilder();
        builder.append(test.getName());
        for (int i = 0; i < userAnswers.length; i++) {
            builder.append(String.format(
                    "\n\t%s \n\t\tОтвет пользователя: %s",
                    test.getExercises()[i],
                    test.getExercises()[i].getAnswerOptions()[userAnswers[i]]
            ));
        }
        return builder.toString();
    }

    public String resultsToString(){
        return String.format(
                "Всего вопросов: %d \nКоличество правильных ответов: %d",
                exerciseCount,
                trueAnswersCount);
    }

    @Override
    public String toString(){
        return test.getName() + " " + trueAnswersCount + " из " + exerciseCount;
    }
}
