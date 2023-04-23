import QtQuick
import QtQuick.Window

import contact 1.0

Window {
    width: 640
    height: 480
    visible: true
    title: qsTr("Hello World")
    color: "#161618"


    ListView{
        id: listView
        anchors{top: topbar.bottom; bottom: parent.bottom; left: parent.left; right: parent.right; topMargin: 10}
        spacing: 10
        model: ContactModel{}
        section.property: "name"
        section.criteria: ViewSection.FirstCharacter
        section.delegate: Text{text: section; color: "white"; padding: 10; font.pointSize: 14}
        delegate:
            Rectangle{
            anchors{ left: parent.left; right: parent.right; margins: 10;}
            height: childrenRect.height
            color: "#212124"
            radius: 12
            Column{
                padding: 20
                width: parent.width
                Text {
                    text: model.name
                    font.pointSize: 16
                    color: "white"
                }
                Text {
                    text: model.number
                    font.pointSize: 14
                    leftPadding: 10
                    color: "white"
                }
            }
        }
    }
    Rectangle{
        id: topbar
        width: parent.width
        anchors.top: parent.top
        height: 60
        color: "#212124"

        Text {
            text: "Contacts"
            font.pointSize: 18
            anchors.fill: parent
            anchors.margins: 20
            color: "white"
        }
    }
}
