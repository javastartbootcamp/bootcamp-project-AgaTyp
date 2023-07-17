package pl.javastart.bootcamp.domain.admin.template;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.javastart.bootcamp.config.notfound.ResourceNotFoundException;
import pl.javastart.bootcamp.domain.training.lesson.Lesson;
import pl.javastart.bootcamp.domain.training.lesson.LessonRepository;
import pl.javastart.bootcamp.utils.YtLinksParser;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TrainingTemplateService {

    private final TrainingTemplateRepository trainingTemplateRepository;
    private final TrainingTemplateLessonRepository trainingTemplateLessonRepository;
    private final LessonRepository lessonRepository;
    private final YtLinksParser ytLinksParser;

    public TrainingTemplateService(TrainingTemplateRepository trainingTemplateRepository,
                                   TrainingTemplateLessonRepository trainingTemplateLessonRepository,
                                   LessonRepository lessonRepository, YtLinksParser ytLinksParser) {
        this.trainingTemplateRepository = trainingTemplateRepository;
        this.trainingTemplateLessonRepository = trainingTemplateLessonRepository;
        this.lessonRepository = lessonRepository;
        this.ytLinksParser = ytLinksParser;
    }


    public List<TrainingTemplate> findAll() {
        return trainingTemplateRepository.findAll();
    }

    public void insert(TrainingTemplate trainingTemplate) {
        trainingTemplateRepository.save(trainingTemplate);
    }

    public TrainingTemplate findByIdOrThrow(Long id) {
        return trainingTemplateRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public TrainingTemplateLesson insertLesson(TrainingTemplateLessonDto dto) {
        TrainingTemplateLesson trainingTemplateLesson = new TrainingTemplateLesson();

        TrainingTemplate trainingTemplate = trainingTemplateRepository.findById(dto.getTrainingTemplateId()).orElseThrow();

        trainingTemplateLesson.setNumber(dto.getNumber());
        trainingTemplateLesson.setTrainingTemplate(trainingTemplate);

        Lesson lesson = new Lesson();
        lesson.setTitle(dto.getTitle());
        lesson.setLessonLinks(dto.getLessonLinks());
        lesson.setVideoLinks(dto.getVideoLinks());
        lessonRepository.save(lesson);

        trainingTemplateLesson.setLesson(lesson);

        trainingTemplateLessonRepository.save(trainingTemplateLesson);

        return trainingTemplateLesson;
    }

    public TrainingTemplateLesson findTemplateLessonByIdOrThrow(Long id) {
        return trainingTemplateLessonRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void updateTemplateLesson(TrainingTemplateLessonDto dto) {
        TrainingTemplateLesson templateLesson = findTemplateLessonByIdOrThrow(dto.getId());
        templateLesson.setNumber(dto.getNumber());
        Lesson lesson = templateLesson.getLesson();
        lesson.setTitle(dto.getTitle());
        if (!StringUtils.isEmpty(dto.getVideoLinks())) {
            List<String> newVideoLinks = ytLinksParser.parseVideoLinks(dto.getVideoLinks().split("\n"));
            lesson.setVideoLinks(String.join("\n", newVideoLinks));
        } else {
            lesson.setVideoLinks(dto.getVideoLinks());
        }
        lesson.setLessonLinks(dto.getLessonLinks());
        dto.setTitle(templateLesson.getLesson().getTitle());
    }

    public TrainingTemplateLessonDto createTemplateLessonForTemplateId(Long templateId) {
        TrainingTemplateLessonDto dto = new TrainingTemplateLessonDto();
        dto.setTrainingTemplateId(templateId);
        List<TrainingTemplateLesson> allForTemplate = trainingTemplateLessonRepository.findByTrainingTemplateIdOrderByNumber(templateId);

        long number = 1;
        if (!allForTemplate.isEmpty()) {
            number = allForTemplate.get(allForTemplate.size() - 1).getNumber() + 1;
        }

        dto.setNumber(number);
        return dto;
    }
}
