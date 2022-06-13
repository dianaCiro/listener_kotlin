package com.attlasian.listener.model

class Task constructor(
    var id: Long,
    var dashboardId: Long,
    var status: String,
    var description: String,
    var startDate: String,
    var endDate: String
)
