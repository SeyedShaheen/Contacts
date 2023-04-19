import QtQuick
import QtQuick.Window

import contact

Window {
    width: 640
    height: 480
    visible: true
    title: qsTr("Hello World")

    ListView{
    anchors.fill: parent

    model: ContactModel{}

    delegate: Column{
        width: parent.width

        Text {
            text: model.name
        }
        Text {
            text: model.number
        }
    }
    }
    }
}
