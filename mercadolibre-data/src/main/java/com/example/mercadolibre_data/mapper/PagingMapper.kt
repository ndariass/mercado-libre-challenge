package com.example.mercadolibre_data.mapper

import com.example.mercadolibre_data.dto.PagingDto
import com.example.mercadolibre_domain.model.Paging
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mapper class to create instances of [Paging]
 *
 * @author Nicolás Arias
 */
@Singleton
class PagingMapper @Inject constructor() {

    /**
     * Maps the given instance of [PagingDto] to an instance of [Paging]. If any of the fields is
     * null then the result of this method is null
     *
     * @param pagingDto the object to map from
     * @return a new instance of [Paging] or null in case the mapping cannot be performed
     */
    fun map(pagingDto: PagingDto?): Paging? =
        pagingDto?.run {
            if (total != null && offset != null && limit != null) {
                Paging(
                    totalElements = total,
                    offset = offset,
                    pageSize = limit
                )
            } else {
                null
            }
        }
}
