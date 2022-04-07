import cv2
import base64
import numpy as np

def main(data):

    decode_data = base64.b64decode(data)
    np_data = np.fromstring(decode_data,np.uint8)
    img = cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)

    width = 350
    height = 450
    dim = (width, height)

    # resize image
    img = cv2.resize(img, dim, interpolation = cv2.INTER_AREA)

    best_score = 0
    image = None
    kp1, kp2, mp, tg = None, None, None, None


    fingerprint_image = cv2.imread("app/src/main/python/orig2/leftmiddle.bmp")

    sift = cv2.SIFT_create()

    keypoints_1, descriptors_1 = sift.detectAndCompute(img, None)
    keypoints_2, descriptors_2 = sift.detectAndCompute(img, None)

    matches1 = cv2.FlannBasedMatcher({'algorithm': 1, 'trees': 10}, {}).knnMatch(descriptors_1, descriptors_2, k=2)

    bf = cv2.BFMatcher(cv2.NORM_L1, crossCheck=False)

    # Perform the matching between the SIFT descriptors of the training image and the test image
    matches = bf.match(descriptors_1, descriptors_2)

    # The matches with shorter distance are the ones we want.
    matches = sorted(matches, key=lambda x: x.distance)

    match_points = []

    for p, q in matches1:
        if p.distance < 0.1 * q.distance:
            match_points.append(p)

    keypoints = 0
    if len(keypoints_1) < len(keypoints_2):
        keypoints = len(keypoints_1)
    else:
        keypoints = len(keypoints_2)

    if len(match_points) / keypoints * 100 > best_score:
        best_score = len(match_points) / keypoints * 100
        image = fingerprint_image
        kp1, kp2, mp = keypoints_1, keypoints_2, match_points
        return "Verified"
    else:
        print("No Fingerprint Match Found")
        return "Not Verified"

