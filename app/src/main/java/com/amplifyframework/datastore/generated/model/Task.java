package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
@Index(name = "task", fields = {"teamID"})
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField BODY = field("Task", "body");
  public static final QueryField STATE = field("Task", "state");
  public static final QueryField TEAM = field("Task", "teamID");
  public static final QueryField TASK_IMAGE_KEY = field("Task", "taskImageKey");
  public static final QueryField TASK_LATITUDE = field("Task", "taskLatitude");
  public static final QueryField TASK_LONGITUDE = field("Task", "taskLongitude");
  public static final QueryField CITY = field("Task", "city");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="String") String state;
  private final @ModelField(targetType="Team") @BelongsTo(targetName = "teamID", type = Team.class) Team team;
  private final @ModelField(targetType="String") String taskImageKey;
  private final @ModelField(targetType="String") String taskLatitude;
  private final @ModelField(targetType="String") String taskLongitude;
  private final @ModelField(targetType="String") String city;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }
  
  public String getState() {
      return state;
  }
  
  public Team getTeam() {
      return team;
  }
  
  public String getTaskImageKey() {
      return taskImageKey;
  }
  
  public String getTaskLatitude() {
      return taskLatitude;
  }
  
  public String getTaskLongitude() {
      return taskLongitude;
  }
  
  public String getCity() {
      return city;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Task(String id, String title, String body, String state, Team team, String taskImageKey, String taskLatitude, String taskLongitude, String city) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.state = state;
    this.team = team;
    this.taskImageKey = taskImageKey;
    this.taskLatitude = taskLatitude;
    this.taskLongitude = taskLongitude;
    this.city = city;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getBody(), task.getBody()) &&
              ObjectsCompat.equals(getState(), task.getState()) &&
              ObjectsCompat.equals(getTeam(), task.getTeam()) &&
              ObjectsCompat.equals(getTaskImageKey(), task.getTaskImageKey()) &&
              ObjectsCompat.equals(getTaskLatitude(), task.getTaskLatitude()) &&
              ObjectsCompat.equals(getTaskLongitude(), task.getTaskLongitude()) &&
              ObjectsCompat.equals(getCity(), task.getCity()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getBody())
      .append(getState())
      .append(getTeam())
      .append(getTaskImageKey())
      .append(getTaskLatitude())
      .append(getTaskLongitude())
      .append(getCity())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("state=" + String.valueOf(getState()) + ", ")
      .append("team=" + String.valueOf(getTeam()) + ", ")
      .append("taskImageKey=" + String.valueOf(getTaskImageKey()) + ", ")
      .append("taskLatitude=" + String.valueOf(getTaskLatitude()) + ", ")
      .append("taskLongitude=" + String.valueOf(getTaskLongitude()) + ", ")
      .append("city=" + String.valueOf(getCity()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      body,
      state,
      team,
      taskImageKey,
      taskLatitude,
      taskLongitude,
      city);
  }
  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep body(String body);
    BuildStep state(String state);
    BuildStep team(Team team);
    BuildStep taskImageKey(String taskImageKey);
    BuildStep taskLatitude(String taskLatitude);
    BuildStep taskLongitude(String taskLongitude);
    BuildStep city(String city);
  }
  

  public static class Builder implements TitleStep, BuildStep {
    private String id;
    private String title;
    private String body;
    private String state;
    private Team team;
    private String taskImageKey;
    private String taskLatitude;
    private String taskLongitude;
    private String city;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          body,
          state,
          team,
          taskImageKey,
          taskLatitude,
          taskLongitude,
          city);
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep state(String state) {
        this.state = state;
        return this;
    }
    
    @Override
     public BuildStep team(Team team) {
        this.team = team;
        return this;
    }
    
    @Override
     public BuildStep taskImageKey(String taskImageKey) {
        this.taskImageKey = taskImageKey;
        return this;
    }
    
    @Override
     public BuildStep taskLatitude(String taskLatitude) {
        this.taskLatitude = taskLatitude;
        return this;
    }
    
    @Override
     public BuildStep taskLongitude(String taskLongitude) {
        this.taskLongitude = taskLongitude;
        return this;
    }
    
    @Override
     public BuildStep city(String city) {
        this.city = city;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String body, String state, Team team, String taskImageKey, String taskLatitude, String taskLongitude, String city) {
      super.id(id);
      super.title(title)
        .body(body)
        .state(state)
        .team(team)
        .taskImageKey(taskImageKey)
        .taskLatitude(taskLatitude)
        .taskLongitude(taskLongitude)
        .city(city);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder state(String state) {
      return (CopyOfBuilder) super.state(state);
    }
    
    @Override
     public CopyOfBuilder team(Team team) {
      return (CopyOfBuilder) super.team(team);
    }
    
    @Override
     public CopyOfBuilder taskImageKey(String taskImageKey) {
      return (CopyOfBuilder) super.taskImageKey(taskImageKey);
    }
    
    @Override
     public CopyOfBuilder taskLatitude(String taskLatitude) {
      return (CopyOfBuilder) super.taskLatitude(taskLatitude);
    }
    
    @Override
     public CopyOfBuilder taskLongitude(String taskLongitude) {
      return (CopyOfBuilder) super.taskLongitude(taskLongitude);
    }
    
    @Override
     public CopyOfBuilder city(String city) {
      return (CopyOfBuilder) super.city(city);
    }
  }
  
}
