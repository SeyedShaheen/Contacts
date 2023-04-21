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
    anchors.fill: parent
    spacing: 10

    model: ContactModel{}

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
            font.pointSize: 18
            color: "white"
        }
        Text {
            text: model.number
            font.pointSize: 16
            leftPadding: 10
            color: "white"
        }
    }
  }
 }
}
