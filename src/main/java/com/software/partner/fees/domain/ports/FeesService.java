package com.software.partner.fees.domain.ports;

import com.software.partner.fees.domain.dto.ParentQuery;
import com.software.partner.fees.domain.dto.ParentFeesResponse;
import com.software.partner.fees.domain.dto.SchoolQuery;
import com.software.partner.fees.domain.dto.SchoolFeesResponse;

public interface FeesService {

    SchoolFeesResponse schoolFees(SchoolQuery schoolQuery);

    ParentFeesResponse parentFees(ParentQuery parentQuery);
}
