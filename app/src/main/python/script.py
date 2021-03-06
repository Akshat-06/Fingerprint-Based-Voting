import cv2
import base64
import numpy as np


def main(data, data1):
    decode_data = base64.b64decode(data)
    np_data = np.fromstring(decode_data, np.uint8)
    sample1 = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)
    print("data of image 1")

    print('Original Dimensions of 1 : ', sample1.shape)

    decode_data2 = base64.b64decode(data1)
    np_data1 = np.fromstring(decode_data2, np.uint8)
    sample2 = cv2.imdecode(np_data1, cv2.IMREAD_UNCHANGED)
    print("data of image 2")

    print('Original Dimensions of 2: ', sample2.shape)

    best_score = 0

    # try:
    for i in range(5):
        sift = cv2.SIFT_create()

        keypoints_1, descriptors_1 = sift.detectAndCompute(sample1, None)
        keypoints_2, descriptors_2 = sift.detectAndCompute(sample2, None)

        matches1 = cv2.FlannBasedMatcher({'algorithm': 1, 'trees': 10}, {}).knnMatch(descriptors_1,
                                                                                     descriptors_2, k=2)

        match_points = []

        for p, q in matches1:
            if p.distance < 0.5 * q.distance:
                match_points.append([p])
                print("for Loop")

        if len(keypoints_1) < len(keypoints_2):
            keypoints = len(keypoints_1)
            print("Keypoint 1:" + str(keypoints))
        else:
            keypoints = len(keypoints_2)
            print("Keypoint 2:" + str(keypoints))

        if len(match_points) / keypoints * 100 > 0:
            best_score = len(match_points) / keypoints * 100
            print("Match Score1:" + str(best_score))
            print(len(match_points))
            return "verified \n" + str(best_score)
        else:
            print(len(match_points))
            print("No Fingerprint Match Found")
            return "Not Verified"
    # except:
    #     return "unable to detect fingerprint or insufficient memory"
