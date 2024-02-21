package com.software.partner.fees.adapter.api;

import com.software.partner.fees.domain.dto.ParentQuery;
import com.software.partner.fees.domain.dto.ParentFeesResponse;
import com.software.partner.fees.domain.dto.SchoolQuery;
import com.software.partner.fees.domain.dto.SchoolFeesResponse;
import com.software.partner.fees.domain.ports.FeesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/fees", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeesController {

    private final FeesService feesService;

    public FeesController(FeesService feesService) {
        this.feesService = feesService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getFeesForSchool() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<SchoolFeesResponse> getFeesForSchool(@PathVariable String schoolId) {
        SchoolFeesResponse schoolFeesResponse = feesService.schoolFees(SchoolQuery.builder()
                .schoolId(UUID.fromString(schoolId))
                .build());

        log.info("Returning fees value for school: {}", schoolFeesResponse);
        return ResponseEntity.ok(schoolFeesResponse);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<ParentFeesResponse> getFeesForParent(@PathVariable String parentId) {
        ParentFeesResponse parentFeesResponse = feesService.parentFees(ParentQuery.builder()
                .parentId(UUID.fromString(parentId))
                .build());

        log.info("Returning fees value for parent: {}", parentFeesResponse);
        return ResponseEntity.ok(parentFeesResponse);
    }
}
