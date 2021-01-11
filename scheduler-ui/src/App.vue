<template>
  <JqxScheduler ref="myScheduler"
                :width="width" :height="600" :source="dataAdapter" :date="date" :view="'weekView'" :resources="resources"
                :appointmentDataFields="appointmentDataFields" :views="views" :showLegend="true" :dayNameFormat="'abbr'" />
</template>

<script>
import JqxScheduler from 'jqwidgets-scripts/jqwidgets-vue/vue_jqxscheduler.vue'

export default {
  components: {
    JqxScheduler
  },
  data: function () {
    return {
      width: 800,
      // eslint-disable-next-line
      date: new jqx.date(),
      appointmentDataFields:
          {
            from: 'start',
            to: 'end',
            id: 'id',
            description: 'description',
            location: 'place',
            subject: 'subject',
            resourceId: 'calendar'
          },
      resources:
          {
            colorScheme: 'scheme05',
            dataField: 'calendar',
            orientation: 'horizontal',
            // eslint-disable-next-line
            source: new jqx.dataAdapter(this.source)
          },
      views:
          [
            { type: 'dayView', showWeekends: false },
            { type: 'weekView', showWeekends: false },
            { type: 'monthView' }
          ],
      // eslint-disable-next-line
      dataAdapter: new jqx.dataAdapter(this.source)
    }
  },
  beforeCreate() {
    this.source = {
      dataType: 'json',
      dataFields: [
        {name: 'id', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'location', type: 'string'},
        {name: 'subject', type: 'string'},
        {name: 'calendar', type: 'string'},
        {name: 'start', type: 'date', format: 'yyyy-MM-dd HH:mm'},
        {name: 'end', type: 'date', format: 'yyyy-MM-dd HH:mm'}
      ],
      id: 'id',
      url: "http://localhost:3001/appointments/"
    };

  },
  mounted: function () {
    this.$refs.myScheduler.focus();
  }
}
</script>