package pl.javastart.bootcamp.domain.admin.topic;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.javastart.bootcamp.domain.admin.task.ReorderDto;
import pl.javastart.bootcamp.utils.ReorderService;
import pl.javastart.bootcamp.utils.YtLinksParser;

import java.util.List;

@Service
public class TopicService {

    private TopicRepository topicRepository;
    private final YtLinksParser ytLinksParser;

    public TopicService(TopicRepository topicRepository, YtLinksParser ytLinksParser) {
        this.topicRepository = topicRepository;
        this.ytLinksParser = ytLinksParser;
    }

    public List<Topic> findAll() {
        return topicRepository.findByOrderBySortOrder();
    }

    public Topic findByIdOrThrow(Long id) {
        return topicRepository.findById(id).orElseThrow();
    }

    public void save(Topic topic) {
        if (!StringUtils.isEmpty(topic.getVideoLinks())) {
            List<String> newVideoLinks = ytLinksParser.parseVideoLinks(topic.getVideoLinks().split("\n"));
            topic.setVideoLinks(String.join("\n", newVideoLinks));
        }

        topicRepository.save(topic);
    }

    public Topic prepareTopicWithSortOrder() {
        Topic topic = new Topic();
        List<Topic> all = findAll();
        long sortOrder = 100;
        if (!all.isEmpty()) {
            sortOrder = all.get(all.size() - 1).getSortOrder() + 100;
        }
        topic.setSortOrder(sortOrder);
        return topic;
    }

    public void reorder(ReorderDto reorderDto) {
        ReorderService<Topic> reorderService = new ReorderService<>(topicRepository);
        reorderService.moveItem(reorderDto.getItemId(), reorderDto.getTargetPosition());
    }
}
