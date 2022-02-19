package es.urjc.dad.devNest.Database.Entities;

import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class GamejamEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private UserEntity adminUser;

    @Lob
    @Column(length = 512)
    private String description;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @OneToMany(mappedBy = "gamejam")
    private List<TeamEntity> teams;

    @ManyToOne
    private TeamEntity winner;


    //region CONSTRUCTOR
    public GamejamEntity() {
    }

    public GamejamEntity(String _name, UserEntity _userEntity, String _topic, Date _startDate, Date _endDate) {
        super();
        name = _name;
        adminUser = _userEntity;
        topic = _topic;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        startDate = formatter.format(_startDate);
        endDate = formatter.format(_endDate);
    }
    public GamejamEntity(String _name, UserEntity _userEntity,String _description, String _topic, Date _startDate, Date _endDate, TeamEntity _winner) {
        super();
        name = _name;
        adminUser = _userEntity;
        topic = _topic;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        startDate = formatter.format(_startDate);
        endDate = formatter.format(_endDate);
        description=_description;
        winner = _winner;
    }
    //endregion

    //region SETTERS

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAdminUser(UserEntity adminUser) {
        this.adminUser = adminUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setTeams(List<TeamEntity> teams) {
        this.teams = teams;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setWinner(TeamEntity _winner) {
        this.winner = _winner;
    }
    //endregion

    //region GETTER

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserEntity getAdminUser() {
        return adminUser;
    }

    public List<TeamEntity> getTeams() {
        return teams;
    }

    public String getTopic() {
        return topic;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public TeamEntity getWinner() {
        return winner;
    }
    //endregion

    @Override
	public String toString() {
		return "Game Jam [name=" + name + ", topic=" + topic + ", admin=" + adminUser + ", start date=" + startDate + ", deadline=" + endDate + "]";
	}
}
