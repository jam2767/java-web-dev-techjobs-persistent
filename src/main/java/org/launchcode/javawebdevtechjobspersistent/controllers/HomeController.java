package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @RequestMapping()
    public String index(Model model, @RequestParam(required = false) Integer employerId) {

        if (employerId == null) {
            model.addAttribute("title", "My Jobs");
            model.addAttribute("employers", employerRepository.findAll());
            model.addAttribute("jobs", jobRepository.findAll());
        } else {
            Optional<Employer> result = employerRepository.findById(employerId);
            if (result.isEmpty()) {
                model.addAttribute("title", employerId);
            } else {
                Employer employer = result.get();
                model.addAttribute("title", employer.getName());
                model.addAttribute("employers", employer.getJobs());
            }
        }
        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }


        Optional optEmployer = employerRepository.findById(employerId);
        if (optEmployer.isPresent()) {
            Employer employer = (Employer) optEmployer.get();
            model.addAttribute("employers", employer);
            model.addAttribute("job", newJob);
            newJob.setEmployer(employer);

            List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
            newJob.setSkills(skillObjs);

            jobRepository.save(newJob);
            return "redirect:/";
        } else {
            return "employers/view";
        }

    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        Optional optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = (Job) optJob.get();
            model.addAttribute("job", job);
            return "view";
        } else {
            return "redirect:../";
        }
    }

//    @GetMapping("employers/view/{employerId}")
//    public String displayViewEmployer(Model model, @PathVariable int employerId) {
//        Optional optEmployer = employerRepository.findById(employerId);
//        if (optEmployer.isPresent()) {
//            Employer employer = (Employer) optEmployer.get();
//            model.addAttribute("employers", employer);
//            return "employers/view";
//        } else {
//            return "redirect:../";
//        }
//    }

    @GetMapping("employers/view/{employerId}")
    public String displayViewEmployer(Model model, @PathVariable int employerId) {
        Employer employer = employerRepository.findById(employerId).get();
        model.addAttribute("employer",employer);
        return "employers/view";
    }

//    @GetMapping("skills/view/{skillId}")
//    public String displayViewSkill(Model model, @PathVariable int skillId) {
//        Skill skill = skillRepository.findById(skillId).get();
//        model.addAttribute("skill", skill);
//        return "skills/view";
//    }

    @GetMapping("skills/view/{skillId}")
    public String displayViewSkill(Model model, @PathVariable int skillId) {

        //Optional optSkill = skillRepository.findById(skillId);
//        if (optSkill.isPresent()) {
//            Skill skill = (Skill) optSkill.get();
//            model.addAttribute("skills", skill);
//            return "skills/view";
//        } else {
//            return "redirect:../";
//        }
        Skill skill = skillRepository.findById(skillId).get();
        model.addAttribute("skill", skill);
        return "skills/view";
    }

//    @GetMapping("view/{Id}")
//    public String displayView(Model model, @PathVariable int Id) {
//        Optional optJob = jobRepository.findById(Id);
//        Optional optEmployer = employerRepository.findById(Id);
//        Optional optSkill = skillRepository.findById(Id);
//
//        if (optJob.isPresent()) {
//            Job job = (Job) optJob.get();
//            model.addAttribute("job", job);
//            return "view";
//        } else if (optEmployer.isPresent()) {
//            Employer employer = (Employer) optEmployer.get();
//            model.addAttribute("employers", employer);
//            return "employers/view";
//        } else if (optSkill.isPresent()) {
//            Skill skill = (Skill) optSkill.get();
//            model.addAttribute("skills", skill);
//            return "skills/view";
//        } else {
//            return "redirect:../";
//        }
//    }

}
