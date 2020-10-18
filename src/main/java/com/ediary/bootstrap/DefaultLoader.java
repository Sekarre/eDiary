package com.ediary.bootstrap;

import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.domain.security.User;
import com.ediary.repositories.*;
import com.ediary.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultLoader implements CommandLineRunner {

    private final AddressRepository addressRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentCouncilRepository parentCouncilRepository;
    private final StudentCouncilRepository studentCouncilRepository;
    private final ExtenuationRepository extenuationRepository;
    private final AttendanceRepository attendanceRepository;
    private final MessageRepository messageRepository;
    private final NoticeRepository noticeRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;

    private Long firstUserAsStudentId;
    private Long firstUserAsParentId;
    private Long firstUserAsTeacherId;

    private final String[] streetNames = {"Odrodzenia", "Jagiellońska", "Kaszubska", "Jana Pawła", "Borysza",
            "Wielkopolska", "Podhalańska", "Odrodzenia", "Borysza", "Mazowiecka",
            "Kujawska", "Mazowiecka", "Wolności", "Wysoka", "Borysza"};

    private final String[] phoneNumber = {"502571821", "515556095", "505735402", "575120854", "455558291",
            "785152102", "735592154", "509124854", "571943111", "710430235",
            "509570183", "730852128", "515152532", "593561943", "920153853"};

    private final String[] studentNames = {"Dagmara", "Daniel", "Alicja", "Olgierd", "Kamil", "Olga", "Michał"};
    private final String[] studentLastNames = {"Lis", "Mazur", "Skrzeczkowska", "Szymczak", "Kubiak", "Woźniak", "Kalinowski"};

    private final String[] parentNames = {"Marian", "Ludwik", "Konrad", "Amir", "Irena", "Rafał", "Jan"};
    private final String[] parentLastNames = {"Lis", "Mazur", "Skrzeczkowski", "Szymczak", "Kubiak", "Woźniak", "Kalinowski"};

    private final String[] teacherNames = {"Czesław", "Rafał", "Maciej", "Magdalena", "Krzysztoł", "Katarzyna"};
    private final String[] teacherLastNames = {"Wójcik", "Marciniak", "Jankowski", "Kowalska", "Michalak", "Sikora"};

    private final String[] classNames = {"1a", "1c", "1g"};

    private final String[] subjectNames = {"Matematyka", "Historia", "Informatyka", "Plastyka", "Muzyka",
            "Język angielski", "Wychowanie fizyczne", "Przyroda", "Biologia", "Technika"};

    private int numberOfParents = parentNames.length;
    private int numberOfStudents = studentNames.length;
    private int numberOfTeachers = teacherNames.length;
    private int numberOfClasses = classNames.length;


    @Override
    public void run(String... args) {
        log.debug("-------------- Bootstrap --------------");

        createAddresses();
        createSchool();
        createUsers();
        createStudentsAndParents();
        createTeachers();
        createParentCouncils();
        createStudentCouncils();
        createExtenuations();
        createAttendances();
        createMessages();
        createNotices();
        createClasses();
        createSubjects();

    }

    private void createAddresses() {
        String state = "Poland";
        String[] cityNames = {"Szczecin"};
        String zip = "70-670";


        for(int i = 0; i < streetNames.length; i++) {
            addressRepository.save(Address.builder()
                    .state(state)
                    .city(cityNames[0])
                    .zip(zip)
                    .street(streetNames[i])
                    .phoneNumber(phoneNumber[i])
                    .build());
        }

        log.debug("-------- Created addresses: " + addressRepository.count());
    }



    private void createSchool() {
        schoolRepository.save(School.builder()
                .name("Darwin College")
                .headmasterName("Danuta")
                .headmasterLastName("Adamska")
                .email("d.adamska@dc.com")
                .schoolOffice("")
                .address(addressRepository.findByStreetAndPhoneNumber(streetNames[0], phoneNumber[0]))
                .build());
    }

    private void createUsers() {

        //todo: username, password (with encoder)

        //Students
        for (int i = 0; i < studentNames.length; i++) {
            userRepository.save(User.builder()
                    .firstName(studentNames[i])
                    .lastName(studentLastNames[i])
                    .username("")
                    .password("")
                    .build());
        }


        //Parents
        Long addressId = addressRepository.findByStreetAndPhoneNumber(streetNames[1], phoneNumber[1]).getId();
        for (int i = 0; i < parentNames.length; i++, addressId++) {
            userRepository.save(User.builder()
                    .firstName(parentNames[i])
                    .lastName(parentLastNames[i])
                    .username("")
                    .password("")
                    .address(addressRepository.findById(addressId).orElse(null))
                    .build());

        }

        //Teachers
        addressId = addressRepository.findByStreetAndPhoneNumber(streetNames[8], phoneNumber[8]).getId();
        for (int i = 0; i < teacherNames.length; i++) {
            userRepository.save(User.builder()
                    .firstName(teacherNames[i])
                    .lastName(teacherLastNames[i])
                    .username("")
                    .password("")
                    .address(addressRepository.findById(addressId++).orElse(null))
                    .build());
        }

        firstUserAsStudentId = userRepository.findByFirstNameAndLastName(studentNames[0], studentLastNames[0]).getId();
        firstUserAsParentId = userRepository.findByFirstNameAndLastName(parentNames[0], parentLastNames[0]).getId();
        firstUserAsTeacherId = userRepository.findByFirstNameAndLastName(teacherNames[0], teacherLastNames[0]).getId();


        log.debug("-------- Created users: " + userRepository.count());

    }

    //todo: classes for student
    private void createStudentsAndParents() {

        long userAsStudentId = firstUserAsStudentId;
        long userAsParentId = firstUserAsParentId;

        for (; userAsStudentId < firstUserAsStudentId + numberOfStudents; userAsStudentId++, userAsParentId++) {
            studentRepository.save(Student.builder()
                    .user(userRepository.findById(userAsStudentId).orElse(null))
                    .parent(createParent(userAsParentId))
                    .build());
        }

        log.debug("-------- Created students: " + studentRepository.count());
        log.debug("-------- Created parents: " + parentRepository.count());

    }


    //todo: extenuation,
    private Parent createParent(Long userId) {
        return parentRepository.save(Parent.builder()
                .user(userRepository.findById(userId).orElse(null))
                .build());
    }


    private void createTeachers() {

        for (long userAsTeacherId = firstUserAsTeacherId; userAsTeacherId < firstUserAsTeacherId + numberOfTeachers; userAsTeacherId++) {
            teacherRepository.save(Teacher.builder()
                    .user(userRepository.findById(userAsTeacherId).orElse(null))
                    .build());
        }

        log.debug("-------- Created teachers: " + teacherRepository.count());

    }


    private void createParentCouncils() {
        for(int i = 0; i < numberOfClasses; i++) {
            parentCouncilRepository.save(ParentCouncil.builder()
                    .build());
        }
    }


    private void createStudentCouncils() {
        for(int i = 0; i < numberOfClasses; i++) {
            studentCouncilRepository.save(StudentCouncil.builder()
                    .build());
        }
    }



    private void createExtenuations() {

        extenuationRepository.save(Extenuation.builder()
                .parent(parentRepository
                        .findByUserId(userRepository.findByFirstNameAndLastName(parentNames[0], parentLastNames[0]).getId()))
                .status(Extenuation.Status.SENT)
                .build());


        extenuationRepository.save(Extenuation.builder()
                .parent(parentRepository
                        .findByUserId(userRepository.findByFirstNameAndLastName(parentNames[1], parentLastNames[1]).getId()))
                .status(Extenuation.Status.SENT)
                .build());

        log.debug("Created extenuations: " + extenuationRepository.count());
    }



    //todo: lesson
    private void createAttendances() {

        //Attendances for each students
        Attendance.Status[] statuses = {
                Attendance.Status.ABSENT,
                Attendance.Status.ABSENT,
                Attendance.Status.PRESENT,
                Attendance.Status.LATE,
                Attendance.Status.PRESENT,
                Attendance.Status.EXCUSED,
                Attendance.Status.ABSENT,
                Attendance.Status.PRESENT
        };


        for (int i = 0; i < numberOfStudents; i++) {
            attendanceRepository.save(Attendance.builder()
                    .student(studentRepository
                            .findByUserId(userRepository.findByFirstNameAndLastName(studentNames[i], studentLastNames[i]).getId()))
                    .status(statuses[i])
                    .build());

        }

        //todo: later
        //Adding extenuation for 1th and 2nd student
/*
        Attendance attendance1 = attendanceRepository.findByStudent(studentRepository
                .findByUserId(userRepository.findByFirstNameAndLastName(studentNames[0], studentLastNames[0]).getId()));

        attendance1.getExtenuations().add(extenuationRepository.findByParent(parentRepository
                .findByUserId(userRepository.findByFirstNameAndLastName(parentNames[0], parentLastNames[0]).getId())
        ));


        log.debug(attendance1.getExtenuations().toString());
        attendanceRepository.save(attendance1);



        Attendance attendance2 = attendanceRepository.findByStudent(studentRepository
                .findByUserId(userRepository.findByFirstNameAndLastName(studentNames[1], studentLastNames[1]).getId()));

        attendance2.getExtenuations().add(extenuationRepository.findByParent(parentRepository
                .findByUserId(userRepository.findByFirstNameAndLastName(parentNames[1], parentLastNames[1]).getId())
        ));

        attendanceRepository.save(attendance2);

*/

        log.debug("Created attendances: " + attendanceRepository.count());
    }


    private void createMessages() {
        int[] senderIndexes = {0, 0, 1};
        int[] readerIndexes = {3, 3, 3};
        String[] messageTitles = {"Cześć", "Dzień dobry", "Kolejna wiad"};
        String[] messageContens = {"To jest testowa wiadomość", "Dzień dobry wiadomość", "Kolejna wiadomość testowa"};
        Message.Status[] statuses = {Message.Status.SENT, Message.Status.READ, Message.Status.SENT};


        for (int i = 0; i < senderIndexes.length; i++) {

            messageRepository.save(Message.builder()
                    .title(messageTitles[i])
                    .content(messageContens[i])
                    .status(statuses[i])
                    .senders(userRepository.findByFirstNameAndLastName(teacherNames[senderIndexes[i]], teacherLastNames[senderIndexes[i]]))
                    .reader(userRepository.findByFirstNameAndLastName(teacherNames[readerIndexes[i]], teacherLastNames[readerIndexes[i]]))
                    .build());
        }
    }


    private void createNotices() {
        int[] teacherIndexes = {3, 2, 5, 1};
        String[] noticeTitle = {"Nowa wiadomość", "Nowe wydarzenie", "Nowe wydarzenie", "Nowa wiadomość"};
        String[] noticeContent = {"Otrzymałeś nowe wiadomości", "Zaplanowano nowe wydarzenie",
                "Zaplanowano nowe wydarzenie", "Otrzymałeś nowe wiadomości"};


        for (int i = 0; i < teacherIndexes.length; i++) {
            noticeRepository.save(Notice.builder()
                    .title(noticeTitle[i])
                    .content(noticeContent[i])
                    .date(Date.valueOf(LocalDate.now()))
                    .user(userRepository.findByFirstNameAndLastName(teacherNames[teacherIndexes[i]], teacherLastNames[teacherIndexes[i]]))
                    .build());
        }

    }


    private void createClasses() {
        for (int i = 0; i < numberOfClasses; i++) {
            classRepository.save(Class.builder()
                    .name(classNames[i])
                    .teacher(teacherRepository.findByUserId(userRepository
                            .findByFirstNameAndLastName(teacherNames[i], teacherLastNames[i]).getId()))
                    .parentCouncil(parentCouncilRepository.findAll().get(i))
                    .studentCouncil(studentCouncilRepository.findAll().get(i))
                    .build());
        }
    }


    private void createSubjects() {
        //indexes of teachers associated with given subjects
        int[] teacherSubjectIndexes = {0, 2, 0, 3, 4, 5, 2, 1, 1, 3};

        for (int i = 0; i < subjectNames.length; i++) {
            subjectRepository.save(Subject.builder()
                    .name(subjectNames[i])
                    .build());

        }

        //Adding teachers to subjects
        List<Subject> savedSubjects = subjectRepository.findAll();

        for (int i = 0; i < subjectNames.length; i++) {
            savedSubjects.get(i).setTeachers(new HashSet<>(Set.of(
                    teacherRepository.findByUserId(userRepository
                            .findByFirstNameAndLastName(
                                    teacherNames[teacherSubjectIndexes[i]],
                                    teacherLastNames[teacherSubjectIndexes[i]]).getId())
            )));
        }


        subjectRepository.saveAll(savedSubjects);

    }


    





}

