package searchbarcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.User;

import java.util.Objects;

public final class StatusCommand {
    public void performStatus(final CommandInput commandInput, final User user,
                            final ObjectNode commandPromptNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode statsNode = objectMapper.createObjectNode();
        if (Objects.equals(user.getUser().getPaused(), false)) {
            user.getUser().setRemainedTime(user.getUser().getRemainedTime()
                    - commandInput.getTimestamp() + user.getUser().getTimestamp());
        }
        if (user.getUser().getRemainedTime() > 0) {
            statsNode.put("name", user.getUser().getMatchingSong().get(0).getName());
            statsNode.put("remainedTime", user.getUser().getRemainedTime());
            statsNode.put("repeat", user.getUser().getRepeat());
            statsNode.put("shuffle", user.getUser().getShuffle());
            statsNode.put("paused", user.getUser().getPaused());
        } else {
            statsNode.put("name", "");
            statsNode.put("remainedTime", 0);
            statsNode.put("repeat", user.getUser().getRepeat());
            statsNode.put("shuffle", user.getUser().getShuffle());
            user.getUser().setPaused(true);
            statsNode.put("paused", user.getUser().getPaused());
        }
        commandPromptNode.set("stats", statsNode);
        user.getUser().setTimestamp(commandInput.getTimestamp());
    }
}
