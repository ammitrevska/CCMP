# CCMP

## CCMP protocol used in IEEE 802.11i standard, also known as WPA2 or RSN 

* `FrameHeader` - information about the source address and destination address
* `ClearTextFrame` - defined the frame that needs to be encrypted 
* `Protocol` - MIC calculation, endcription and decription of the 128 bits blocks
* `EncryptedFrame` - encripted frame, with Frame Header, data and MIC
* `TestAES` - AES standard for encoding used in ECB mode.


scheme that is implemented
![Diagram-of-the-CCMP-encapsulation-process](https://github.com/ammitrevska/CCMP/assets/94235179/b6bc4630-7bc8-425c-8b5a-861b32c71bf5)
