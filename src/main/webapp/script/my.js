// noinspection JSUnusedGlobalSymbols

function delete_task(task_id) {
    let url = "/" + task_id;
    $.ajax({
        url: url,
        method: 'DELETE',
        success: function () {
            window.location.reload();
        }
    });
}

function edit_task(task_id) {
    let identifier_delete = "#delete_" + task_id;
    $(identifier_delete).remove();

    let identifier_edit = "#edit_" + task_id;
    let property_value = "Save";
    let property_onclick = "update_task(" + task_id + ")";

    $(identifier_edit).attr("value", property_value);
    $(identifier_edit).attr("onclick", property_onclick);

    let current_tr_element = $(identifier_edit).parent().parent();
    let children = current_tr_element.children()
    let td_description = children[1];
    td_description.innerHTML = "<input id='input_description_" + task_id + "' type='text' value='" + td_description.innerHTML + "'>";

    let td_status = children[2];
    let status_current_value = td_status.innerHTML;
    td_status.innerHTML = getDropdownStatusHtml(task_id);
    let status_id = "#select_status_" + task_id;
    $(status_id).val(status_current_value).change();

}

function getDropdownStatusHtml(task_id) {
    let status_id = "'select_status_" + task_id + "'";
    return "<select id=" + status_id + " name = 'status'>"
        + "<option value='IN_PROGRESS'>IN_PROGRESS</option>"
        + "<option value='DONE'>DONE</option>"
        + "<option value='PAUSED'>PAUSED</option>"
        + "</select>";
}


function update_task(task_id) {
    let value_description = $("#input_description_" + task_id).val();
    let value_status = $("#select_status_" + task_id).val();

    let url = "/" + task_id;

    $.ajax({
        url: url,
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        async: false,
        data: JSON.stringify(
            {
                "description": value_description,
                "status": value_status
            })
    });

    setTimeout(() => {
        document.location.reload();
    }, 300);
}

function add_task() {
    let value_description = $("#description_new").val();
    let value_status = $("#status_new").val();

    let url = "/";

    $.ajax({
        url: url,
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        async: false,
        data: JSON.stringify(
            {
                "description": value_description,
                "status": value_status
            }
        )
    });

    setTimeout(() => {
        document.location.reload();
    }, 300);
}
