package data;

import java.util.List;

public class CucumberReport {
    private int line;
    private List<Element> elements;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public class Element {
        private int line;
        private String name;
        private String descriptions;
        private String id;
        private List<Event> afters;
        private String type;
        private String keyword;
        private List<Event> steps;
        private List<Event> befores;
        private List<Tag> tags;

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescriptions() {
            return descriptions;
        }

        public void setDescriptions(String descriptions) {
            this.descriptions = descriptions;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Event> getAfters() {
            return afters;
        }

        public void setAfters(List<Event> afters) {
            this.afters = afters;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public List<Event> getSteps() {
            return steps;
        }

        public void setSteps(List<Event> steps) {
            this.steps = steps;
        }

        public List<Event> getBefores() {
            return befores;
        }

        public void setBefores(List<Event> befores) {
            this.befores = befores;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }
    }

    public class Event {
        private Result result;
        private int line;
        private String name;
        private Match match;
        private String keyword;

        public Result getResult() {
            return result;
        }

        public void setResult(Result results) {
            this.result = results;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Match getMatch() {
            return match;
        }

        public void setMatch(Match match) {
            this.match = match;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }

    public class Result {
        private int duration;
        private String error_message;
        private String status;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getErrorMessage() {
            return error_message;
        }

        public void setErrorMessage(String errorMessage) {
            this.error_message = errorMessage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class Match {
        private String location;
        private List<Arguments> arguments;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<Arguments> getArguments() {
            return arguments;
        }

        public void setArguments(List<Arguments> arguments) {
            this.arguments = arguments;
        }
    }

    public class Arguments {
        private String val;
        private int offset;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

    public class Tag {
        private int line;
        private String name;

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
